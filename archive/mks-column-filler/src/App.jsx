import React, { useState, useCallback, useRef } from "react";
import Papa from "papaparse";

const SYSTEM_PROMPT = `You are an expert Android architecture analyzer. 
Analyze the provided Kotlin file/snippet. You MUST respond with ONLY valid JSON containing the exact following fields:
- "entity_name": The name of the primary class, function, or object.
- "main_role": 2-4 words describing what this code is (e.g. "Data Validator", "Screen Builder").
- "what_it_does": One plain-English sentence, no jargon.
- "auto_detected_type": Technical type (e.g., "DAO", "ViewModel", "Entity").
- "component_type": Architecture layer (e.g., "Data", "UI", "Domain").
- "ai_analysis": A brief 2-3 sentence analysis of the code.
- "refactoring_suggestions": A brief note on possible refactoring, or empty string if none.
- "dependencies": Key dependencies used in this code (e.g., "Room, Hilt, Coroutines").`;

async function callAI(row, config) {
  const prompt = `File: ${row["File Path"] || row["File Name"]}\n\nCode:\n${row["Code Snippet"]}\n\nAnalyze this code and return the requested JSON.`;

  if (["OpenAI-Compatible", "Alibaba", "OpenRouter", "Zenmux"].includes(config.provider)) {
    const res = await fetch(config.url, {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
        ...(config.key ? { "Authorization": `Bearer ${config.key}` } : {})
      },
      body: JSON.stringify({
        model: config.model,
        messages: [
          { role: "system", content: SYSTEM_PROMPT },
          { role: "user", content: prompt }
        ],
        response_format: { type: "json_object" },
        stream: false
      })
    });
    if (!res.ok) throw new Error(`HTTP Error ${res.status}: ${await res.text()}`);
    const data = await res.json();
    let text = data.choices[0].message.content;
    text = text.replace(/^```json\s*/m, "").replace(/^```\s*/m, "").replace(/```\s*$/m, "").trim();
    return JSON.parse(text);
  } 
  else if (config.provider === "Gemini") {
    const url = `${config.url}/v1beta/models/${config.model}:generateContent?key=${config.key}`;
    const res = await fetch(url, {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({
        system_instruction: { parts: [{ text: SYSTEM_PROMPT }] },
        contents: [{ role: "user", parts: [{ text: prompt }] }],
        generationConfig: { responseMimeType: "application/json" }
      })
    });
    if (!res.ok) throw new Error(`HTTP Error ${res.status}: ${await res.text()}`);
    const data = await res.json();
    let text = data.candidates[0].content.parts[0].text;
    return JSON.parse(text);
  }
  else if (config.provider === "Anthropic") {
    const res = await fetch(config.url, {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
        "x-api-key": config.key,
        "anthropic-version": "2023-06-01",
        "anthropic-dangerously-allow-browser": "true"
      },
      body: JSON.stringify({
        model: config.model,
        max_tokens: 1024,
        system: SYSTEM_PROMPT,
        messages: [{ role: "user", content: prompt }]
      })
    });
    if (!res.ok) throw new Error(`HTTP Error ${res.status}: ${await res.text()}`);
    const data = await res.json();
    let text = data.content[0].text;
    text = text.substring(text.indexOf('{'), text.lastIndexOf('}') + 1);
    return JSON.parse(text);
  }
}

const ALL_COLUMNS = [
  { id: "#", label: "#" },
  { id: "ID", label: "ID" },
  { id: "File Name", label: "File" },
  { id: "Entity Name", label: "Entity Name" },
  { id: "Main Role", label: "Main Role" },
  { id: "Auto-detected Type", label: "Auto Type" },
  { id: "Component Type", label: "Component Type" },
  { id: "AI Analysis & Summary", label: "Analysis (preview)" },
  { id: "Status", label: "Status" }
];

export default function App() {
  const [csvData, setCsvData] = useState([]);
  const [headers, setHeaders] = useState([]);
  
  const [config, setConfig] = useState({
    provider: "OpenAI-Compatible",
    url: "http://localhost:11434/v1/chat/completions",
    key: "",
    model: "llama3"
  });

  const [phase, setPhase] = useState("idle");
  const [results, setResults] = useState({});
  const [currentRowIndex, setCurrentRowIndex] = useState(-1);
  const [errLog, setErrLog] = useState([]);
  const abortRef = useRef(false);

  // New states
  const [availableModels, setAvailableModels] = useState([]);
  const [testingConnection, setTestingConnection] = useState(false);
  const [connectionStatus, setConnectionStatus] = useState("");
  const [groupBy, setGroupBy] = useState("None");
  const [visibleColumns, setVisibleColumns] = useState(
    ALL_COLUMNS.reduce((acc, col) => ({ ...acc, [col.id]: true }), {})
  );

  const filledCount = Object.keys(results).length;
  const progress = csvData.length > 0 ? Math.round((filledCount / csvData.length) * 100) : 0;

  const handleFileUpload = (e) => {
    const file = e.target.files[0];
    if (!file) return;
    Papa.parse(file, {
      header: true,
      skipEmptyLines: true,
      complete: (result) => {
        setHeaders(result.meta.fields);
        const validRows = result.data.filter(r => r["ID"] || r["File Name"]);
        setCsvData(validRows);
        setResults({});
        setErrLog([]);
        setPhase("idle");
      },
      error: (error) => setErrLog([`CSV Parse Error: ${error.message}`])
    });
  };

  const testConnectionAndFetchModels = async () => {
    setTestingConnection(true);
    setConnectionStatus("Testing...");
    try {
      if (["OpenAI-Compatible", "Alibaba", "OpenRouter", "Zenmux"].includes(config.provider)) {
        let baseUrl = config.url.replace(/\/chat\/completions\/?$/, "");
        if (!baseUrl.endsWith("/models")) {
           if (!baseUrl.endsWith("/")) baseUrl += "/";
           baseUrl += "models";
        }
        
        const res = await fetch(baseUrl, {
          method: "GET",
          headers: {
            "Content-Type": "application/json",
            ...(config.key ? { "Authorization": `Bearer ${config.key}` } : {})
          }
        });
        if (!res.ok) throw new Error(`HTTP Error ${res.status}`);
        const data = await res.json();
        
        let fetchedModels = [];
        if (data && data.data && Array.isArray(data.data)) {
          fetchedModels = data.data.map(m => m.id);
        } else if (data && data.models && Array.isArray(data.models)) {
          fetchedModels = data.models.map(m => m.name);
        }

        if (fetchedModels.length > 0) {
          setAvailableModels(fetchedModels);
          if (!fetchedModels.includes(config.model)) {
             setConfig(c => ({...c, model: fetchedModels[0]}));
          }
          setConnectionStatus(`Success! Found ${fetchedModels.length} models.`);
        } else {
          setConnectionStatus("Connected, but couldn't parse models list.");
        }
      } else {
        setConnectionStatus("Connection test successful. (Model fetch unsupported)");
      }
    } catch (e) {
      setConnectionStatus(`Error: ${e.message}`);
    } finally {
      setTestingConnection(false);
    }
  };

  const getRowStatus = (idx) => {
    if (results[idx]) return results[idx].skip ? "skipped" : "done";
    if (phase === "running" && currentRowIndex === idx) return "processing";
    return "pending";
  };

  const run = useCallback(async () => {
    if (csvData.length === 0) return;
    abortRef.current = false;
    setPhase("running");
    
    for (let i = 0; i < csvData.length; i++) {
      if (abortRef.current) { setPhase("idle"); break; }
      if (results[i]) continue;

      setCurrentRowIndex(i);
      const row = csvData[i];
      let parsed = null;

      if (!row["Code Snippet"] || row["Code Snippet"].trim() === "") {
         setResults(prev => ({ ...prev, [i]: { skip: true } }));
         continue;
      }

      for (let attempt = 0; attempt < 3; attempt++) {
        if (abortRef.current) break;
        try {
          parsed = await callAI(row, config);
          break;
        } catch (e) {
          if (attempt === 2) setErrLog(prev => [...prev, `Row ${i + 1} (${row.ID}): ${e.message}`]);
          else await new Promise(r => setTimeout(r, 2000 * (attempt + 1)));
        }
      }

      if (parsed && !abortRef.current) setResults(prev => ({ ...prev, [i]: parsed }));
    }

    if (!abortRef.current) { setPhase("done"); setCurrentRowIndex(-1); }
  }, [csvData, config, results]);

  const stop = () => { abortRef.current = true; setPhase("idle"); };

  const downloadCSV = useCallback(() => {
    const rows = csvData.map((r, i) => {
      const res = results[i] || {};
      return {
        ...r,
        "Entity Name": res.entity_name || r["Entity Name"] || "",
        "Main Role": res.main_role || r["Main Role"] || "",
        "What It Does": res.what_it_does || r["What It Does"] || "",
        "Auto-detected Type": res.auto_detected_type || r["Auto-detected Type"] || "",
        "Component Type": res.component_type || r["Component Type"] || "",
        "AI Analysis & Summary": res.ai_analysis || r["AI Analysis & Summary"] || "",
        "Refactoring Suggestions": res.refactoring_suggestions || r["Refactoring Suggestions"] || "",
        "Dependencies": res.dependencies || r["Dependencies"] || "",
        "Status": res.skip ? "Skipped" : (res.entity_name ? "AI-Filled" : r["Status"])
      };
    });

    const csvStr = Papa.unparse(rows);
    const blob = new Blob(["\uFEFF" + csvStr], { type: "text/csv;charset=utf-8" });
    const url = URL.createObjectURL(blob);
    Object.assign(document.createElement("a"), { href: url, download: "MKS_Architecture_Filled.csv" }).click();
    URL.revokeObjectURL(url);
  }, [csvData, results]);

  const getGroupedData = () => {
    if (groupBy === "None") return [{ groupName: "", rows: csvData.map((row, i) => ({row, originalIndex: i})) }];
    
    const groups = {};
    csvData.forEach((row, i) => {
      let key = "";
      if (groupBy === "Module") key = row.Module || "Unknown Module";
      if (groupBy === "File Name") key = row["File Name"] || "Unknown File";
      if (groupBy === "Status") key = getRowStatus(i);
      
      if (!groups[key]) groups[key] = [];
      groups[key].push({ row, originalIndex: i });
    });
    
    return Object.keys(groups).sort().map(k => ({ groupName: k, rows: groups[k] }));
  };

  return (
    <div style={{ background: "#0f1117", minHeight: "100vh", color: "#e2e8f0", fontFamily: "Inter, system-ui, sans-serif", padding: "24px" }}>
      <div style={{ marginBottom: 24 }}>
        <h1 style={{ fontSize: 22, fontWeight: 700, color: "#f1f5f9", margin: 0 }}>
          MKS Architecture Sheet — Smart CSV AI Fill
        </h1>
        <p style={{ color: "#64748b", marginTop: 6, fontSize: 14 }}>
          Upload your CSV, configure your AI API (supports local Ollama), and let it process your codebase sequentially.
        </p>
      </div>

      {/* Configuration Panel */}
      <div style={{ display: "flex", gap: "24px", flexWrap: "wrap", marginBottom: 24, background: "#1e293b", padding: 20, borderRadius: 10 }}>
        
        {/* CSV Upload */}
        <div style={{ display: "flex", flexDirection: "column", gap: 8, minWidth: 250 }}>
          <label style={{ fontSize: 13, fontWeight: 600, color: "#94a3b8" }}>1. Load CSV File</label>
          <input type="file" accept=".csv" onChange={handleFileUpload} disabled={phase === "running"} style={{ color: "#e2e8f0", fontSize: 14 }} />
          {csvData.length > 0 && <span style={{ fontSize: 12, color: "#34d399" }}>Loaded {csvData.length} rows successfully.</span>}
        </div>

        {/* API Config */}
        <div style={{ display: "flex", flexDirection: "column", gap: 12, flex: 1, minWidth: 300 }}>
          <div style={{ display: "flex", justifyContent: "space-between", alignItems: "center" }}>
            <label style={{ fontSize: 13, fontWeight: 600, color: "#94a3b8" }}>2. Configure AI Provider</label>
            <button 
              onClick={testConnectionAndFetchModels}
              disabled={testingConnection || phase === "running"}
              style={{ background: "#3b82f6", border: "none", borderRadius: 4, padding: "4px 8px", color: "white", fontSize: 12, cursor: "pointer" }}
            >
              {testingConnection ? "Testing..." : "Test Connection & Fetch Models"}
            </button>
          </div>

          <div style={{ display: "flex", gap: 12, flexWrap: "wrap" }}>
            <select 
              value={config.provider} 
              onChange={e => {
                const val = e.target.value;
                let defaults = { url: "", model: "" };
                if (val === "OpenAI-Compatible") defaults = { url: "http://localhost:11434/v1/chat/completions", model: "llama3" };
                if (val === "Alibaba") defaults = { url: "https://dashscope.aliyuncs.com/compatible-mode/v1/chat/completions", model: "qwen-plus" };
                if (val === "OpenRouter") defaults = { url: "https://openrouter.ai/api/v1/chat/completions", model: "openai/gpt-4o" };
                if (val === "Zenmux") defaults = { url: "https://api.zenmux.com/v1/chat/completions", model: "gpt-4o" };
                if (val === "Gemini") defaults = { url: "https://generativelanguage.googleapis.com", model: "gemini-1.5-flash" };
                if (val === "Anthropic") defaults = { url: "https://api.anthropic.com/v1/messages", model: "claude-3-5-sonnet-20240620" };
                setConfig(c => ({ ...c, provider: val, url: defaults.url, model: defaults.model }));
                setAvailableModels([]);
                setConnectionStatus("");
              }}
              disabled={phase === "running"}
              style={{ background: "#0f1117", border: "1px solid #334155", color: "#e2e8f0", padding: "8px 12px", borderRadius: 6 }}
            >
              <option value="OpenAI-Compatible">OpenAI-Compatible (Ollama, vLLM, OpenAI)</option>
              <option value="Alibaba">Alibaba (Qwen)</option>
              <option value="OpenRouter">OpenRouter</option>
              <option value="Zenmux">Zenmux</option>
              <option value="Gemini">Gemini</option>
              <option value="Anthropic">Anthropic</option>
            </select>

            {availableModels.length > 0 ? (
              <select
                value={config.model}
                onChange={e => setConfig(c => ({ ...c, model: e.target.value }))}
                disabled={phase === "running"}
                style={{ flex: 1, background: "#0f1117", border: "1px solid #334155", color: "#e2e8f0", padding: "8px 12px", borderRadius: 6 }}
              >
                {availableModels.map(m => <option key={m} value={m}>{m}</option>)}
              </select>
            ) : (
              <input 
                type="text" 
                placeholder="Model name (e.g. llama3, gpt-4o)" 
                value={config.model}
                onChange={e => setConfig(c => ({ ...c, model: e.target.value }))}
                disabled={phase === "running"}
                style={{ flex: 1, background: "#0f1117", border: "1px solid #334155", color: "#e2e8f0", padding: "8px 12px", borderRadius: 6 }} 
              />
            )}
          </div>

          <div style={{ display: "flex", gap: 12, flexWrap: "wrap" }}>
            <input 
              type="text" 
              placeholder="Base URL (e.g. http://localhost:11434/v1/chat/completions)" 
              value={config.url}
              onChange={e => setConfig(c => ({ ...c, url: e.target.value }))}
              disabled={phase === "running"}
              style={{ flex: 2, background: "#0f1117", border: "1px solid #334155", color: "#e2e8f0", padding: "8px 12px", borderRadius: 6 }} 
            />
            
            <input 
              type="password" 
              placeholder="API Key (Leave blank for Ollama)" 
              value={config.key}
              onChange={e => setConfig(c => ({ ...c, key: e.target.value }))}
              disabled={phase === "running"}
              style={{ flex: 1, background: "#0f1117", border: "1px solid #334155", color: "#e2e8f0", padding: "8px 12px", borderRadius: 6 }} 
            />
          </div>
          {connectionStatus && <span style={{ fontSize: 12, color: connectionStatus.includes("Error") ? "#f87171" : "#34d399" }}>{connectionStatus}</span>}
        </div>
      </div>

      {/* Advanced Tools: Visibility & Grouping */}
      {csvData.length > 0 && (
        <div style={{ display: "flex", gap: 24, marginBottom: 20, flexWrap: "wrap", background: "#111827", padding: 16, borderRadius: 8 }}>
          <div style={{ flex: 1 }}>
            <label style={{ fontSize: 12, fontWeight: 600, color: "#94a3b8", display: "block", marginBottom: 8 }}>Column Visibility</label>
            <div style={{ display: "flex", gap: 12, flexWrap: "wrap" }}>
              {ALL_COLUMNS.map(col => (
                <label key={col.id} style={{ fontSize: 12, display: "flex", alignItems: "center", gap: 4, cursor: "pointer" }}>
                  <input 
                    type="checkbox" 
                    checked={visibleColumns[col.id]}
                    onChange={() => setVisibleColumns(prev => ({ ...prev, [col.id]: !prev[col.id] }))}
                  />
                  {col.label}
                </label>
              ))}
            </div>
          </div>
          
          <div style={{ minWidth: 200 }}>
             <label style={{ fontSize: 12, fontWeight: 600, color: "#94a3b8", display: "block", marginBottom: 8 }}>Group Rows By</label>
             <select 
                value={groupBy}
                onChange={e => setGroupBy(e.target.value)}
                style={{ background: "#0f1117", border: "1px solid #334155", color: "#e2e8f0", padding: "6px 12px", borderRadius: 4, width: "100%" }}
             >
                <option value="None">None (Default Order)</option>
                <option value="Module">Module</option>
                <option value="File Name">File Name</option>
                <option value="Status">Status</option>
             </select>
          </div>
        </div>
      )}

      {/* Controls */}
      <div style={{ display: "flex", gap: 12, alignItems: "center", marginBottom: 24, flexWrap: "wrap" }}>
        {phase !== "running" ? (
          <button
            onClick={run}
            disabled={csvData.length === 0}
            style={{
              padding: "10px 24px", borderRadius: 8, border: "none", cursor: csvData.length === 0 ? "not-allowed" : "pointer",
              background: csvData.length === 0 ? "#1e293b" : "#3b82f6",
              color: csvData.length === 0 ? "#64748b" : "#fff",
              fontWeight: 600, fontSize: 15, transition: "all 0.2s"
            }}
          >
            ▶ {filledCount > 0 ? "Resume Filling" : "Start Filling"}
          </button>
        ) : (
          <button
            onClick={stop}
            style={{
              padding: "10px 24px", borderRadius: 8, border: "none", cursor: "pointer",
              background: "#ef4444", color: "#fff",
              fontWeight: 600, fontSize: 15, transition: "all 0.2s"
            }}
          >
            ⏹ Stop
          </button>
        )}

        <button
          onClick={downloadCSV}
          disabled={filledCount === 0 && csvData.length === 0}
          style={{
            padding: "10px 24px", borderRadius: 8, border: "1px solid #334155", cursor: (filledCount === 0 && csvData.length === 0) ? "not-allowed" : "pointer",
            background: (filledCount === 0 && csvData.length === 0) ? "#1e293b" : "#064e3b",
            color: (filledCount === 0 && csvData.length === 0) ? "#475569" : "#34d399",
            fontWeight: 600, fontSize: 15
          }}
        >
          ⬇ Download CSV
        </button>
      </div>

      {/* Progress bar */}
      {csvData.length > 0 && (
        <div style={{ marginBottom: 20 }}>
          <div style={{ display: "flex", justifyContent: "space-between", marginBottom: 6, fontSize: 13, color: "#94a3b8" }}>
            <span>{filledCount} of {csvData.length} rows processed</span>
            <span>{progress}%</span>
          </div>
          <div style={{ height: 8, background: "#1e293b", borderRadius: 99, overflow: "hidden" }}>
            <div style={{ height: "100%", width: `${progress}%`, background: "linear-gradient(90deg,#3b82f6,#34d399)", borderRadius: 99, transition: "width 0.4s ease" }} />
          </div>
        </div>
      )}

      {/* Error log */}
      {errLog.length > 0 && (
        <div style={{ background: "#1c0a0a", border: "1px solid #7f1d1d", borderRadius: 8, padding: 12, marginBottom: 16, maxHeight: 150, overflowY: "auto" }}>
          <div style={{ display: "flex", justifyContent: "space-between", alignItems: "center", marginBottom: 8 }}>
            <p style={{ color: "#fca5a5", fontWeight: 600, margin: 0 }}>Errors ({errLog.length})</p>
            <button onClick={() => setErrLog([])} style={{ background: "transparent", border: "none", color: "#991b1b", cursor: "pointer", fontSize: 12 }}>Clear</button>
          </div>
          {errLog.map((e, i) => <p key={i} style={{ color: "#f87171", margin: "2px 0", fontSize: 13 }}>{e}</p>)}
        </div>
      )}

      {/* Table */}
      {csvData.length > 0 && (
        <div style={{ overflowX: "auto", borderRadius: 10, border: "1px solid #1e293b", maxHeight: "60vh" }}>
          <table style={{ width: "100%", borderCollapse: "collapse", fontSize: 13 }}>
            <thead style={{ position: "sticky", top: 0, zIndex: 10 }}>
              <tr style={{ background: "#0f172a" }}>
                {ALL_COLUMNS.filter(c => visibleColumns[c.id]).map(h => (
                  <th key={h.id} style={{ padding: "10px 14px", textAlign: "left", color: "#64748b", fontWeight: 600, borderBottom: "1px solid #1e293b", whiteSpace: "nowrap" }}>{h.label}</th>
                ))}
              </tr>
            </thead>
            <tbody>
              {getGroupedData().map(group => (
                <React.Fragment key={group.groupName}>
                  {group.groupName && (
                    <tr>
                      <td colSpan={ALL_COLUMNS.filter(c => visibleColumns[c.id]).length} style={{ background: "#1e293b", color: "#f8fafc", padding: "8px 14px", fontWeight: 600 }}>
                        {group.groupName} ({group.rows.length} items)
                      </td>
                    </tr>
                  )}
                  {group.rows.map(({row, originalIndex: i}) => {
                    const st = getRowStatus(i);
                    const res = results[i] || {};
                    const isProcessing = st === "processing";
                    const rowBg = isProcessing ? "#0c1829" : i % 2 === 0 ? "#0f1117" : "#111827";
                    
                    if (phase === "running" && Math.abs(currentRowIndex - i) > 50) return null;

                    return (
                      <tr key={i} style={{ background: rowBg, transition: "background 0.3s" }}>
                        {visibleColumns["#"] && <td style={{ padding: "8px 14px", color: "#475569" }}>{i + 1}</td>}
                        {visibleColumns["ID"] && <td style={{ padding: "8px 14px", color: "#94a3b8", fontFamily: "monospace", fontSize: 11 }}>{row.ID}</td>}
                        {visibleColumns["File Name"] && <td style={{ padding: "8px 14px", color: "#cbd5e1", maxWidth: 160, overflow: "hidden", textOverflow: "ellipsis", whiteSpace: "nowrap" }}>{row["File Name"]}</td>}
                        {visibleColumns["Entity Name"] && <td style={{ padding: "8px 14px", color: "#e2e8f0", maxWidth: 140, overflow: "hidden", textOverflow: "ellipsis", whiteSpace: "nowrap" }}>{res.entity_name || row["Entity Name"] || <span style={{ color: "#374151" }}>—</span>}</td>}
                        {visibleColumns["Main Role"] && <td style={{ padding: "8px 14px", color: "#a5b4fc", maxWidth: 180, overflow: "hidden", textOverflow: "ellipsis", whiteSpace: "nowrap" }}>{res.main_role || row["Main Role"] || <span style={{ color: "#374151" }}>—</span>}</td>}
                        {visibleColumns["Auto-detected Type"] && <td style={{ padding: "8px 14px", maxWidth: 130, overflow: "hidden", textOverflow: "ellipsis", whiteSpace: "nowrap" }}><span style={{ background: "#1e293b", color: "#7dd3fc", padding: "2px 7px", borderRadius: 5, fontSize: 11 }}>{res.auto_detected_type || row["Auto-detected Type"] || "—"}</span></td>}
                        {visibleColumns["Component Type"] && <td style={{ padding: "8px 14px", color: "#c4b5fd", maxWidth: 150, overflow: "hidden", textOverflow: "ellipsis", whiteSpace: "nowrap" }}>{res.component_type || row["Component Type"] || <span style={{ color: "#374151" }}>—</span>}</td>}
                        {visibleColumns["AI Analysis & Summary"] && <td style={{ padding: "8px 14px", color: "#94a3b8", maxWidth: 220, overflow: "hidden", textOverflow: "ellipsis", whiteSpace: "nowrap" }}>{res.ai_analysis ? res.ai_analysis.slice(0, 80) + "…" : (row["AI Analysis & Summary"] ? row["AI Analysis & Summary"].slice(0, 80) + "…" : <span style={{ color: "#374151" }}>—</span>)}</td>}
                        {visibleColumns["Status"] && <td style={{ padding: "8px 14px", whiteSpace: "nowrap" }}>
                          {st === "processing" ? <span style={{ color: "#60a5fa", fontSize: 12 }}>⏳ filling…</span> : st === "done" ? <span style={{ color: "#34d399", fontSize: 12 }}>✓ done</span> : st === "skipped" ? <span style={{ color: "#94a3b8", fontSize: 12 }}>⊘ skip</span> : <span style={{ color: "#374151", fontSize: 12 }}>○ pending</span>}
                        </td>}
                      </tr>
                    );
                  })}
                </React.Fragment>
              ))}
            </tbody>
          </table>
        </div>
      )}
    </div>
  );
}
