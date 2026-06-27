# Gemini & MCP Configuration - MKS Project

This guide explains how to enhance the Gemini AI agent in Android Studio by adding **Model Context
Protocol (MCP)** servers. MCP allows Gemini to interact with external tools, services, and local
resources.

## 1. Accessing MCP Settings

To configure MCP servers in Android Studio:

* **macOS:** Go to **Android Studio > Settings > Tools > AI > MCP Servers**.
* **Windows/Linux:** Go to **File > Settings > Tools > AI > MCP Servers**.

## 2. Enabling and Configuring

1. Check the box for **Enable MCP Servers**.
2. In the configuration field, enter your server details in JSON format. This configuration is
   stored in your Android Studio's `mcp.json` file.
3. Click **OK**.

### Configuration Schema

| Option    | Description                                                                        |
|:----------|:-----------------------------------------------------------------------------------|
| `httpUrl` | **Required** for streamable HTTP endpoints (e.g., `https://example.com/mcp`).      |
| `url`     | Use this if the server uses **Server-Sent Events (SSE)** (usually ends in `/sse`). |
| `headers` | A map of custom HTTP headers (e.g., `{"Authorization": "Bearer <TOKEN>"}`).        |
| `timeout` | Connection timeout in milliseconds (-1 for no timeout).                            |

### Example `mcp.json`

```json
{
  "mcpServers": {
    "github": {
      "httpUrl": "https://api.githubcopilot.com/mcp/",
      "headers": {
        "Authorization": "Bearer <YOUR_PERSONAL_ACCESS_TOKEN>"
      }
    },
    "figma": {
      "httpUrl": "https://mcp.figma.com/mcp"
    }
  }
}
```

## 3. Authentication

If a remote server requires authorization (e.g., via OAuth or Bearer token):

1. Android Studio will attempt to connect and may show an error: "Error connecting to transport:
   Authorization Exception."
2. Click **Start Login** in the notification. This will open a login page in your browser.
3. Once signed in, the connection will re-attempt automatically.
4. **Note:** For servers like GitHub, use Personal Access Tokens (PATs) instead of OAuth if
   possible.

## 4. Using MCP Tools in Chat

Once connected, you can use these commands in the Gemini chat:

* **List Tools:** Type `/mcp` to see all available tools and their statuses.
* **Execute:** Ask Gemini naturally to use a tool (e.g., "List my repositories on GitHub" or "Fetch
  design info from Figma").

## 5. Important Limitations

* **No `stdio` support:** Android Studio only supports HTTP/SSE transports. It cannot connect to
  servers via `stdio` (standard I/O).
* **No Resources/Prompts:** MCP resources and prompt templates are not currently supported.
* **Experimental:** MCP support in Android Studio is currently in preview and may change.
