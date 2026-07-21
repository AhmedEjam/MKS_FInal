# Manual Test Plan — AI Provider Routing & Privacy Consent

> Covers three fixes: (1) the prompt-deck run now honours the selected cloud provider instead of
> being hardwired to Ollama, (2) the MCQ generator now gates cloud runs behind privacy consent, and
> (3) the network security config forces cloud traffic to HTTPS and drops user-CA trust in release.
>
> Compile- and R8-verified only. `feature:ui` has no unit-test harness, so the routing branch is not
> unit-tested; verify on a device. Related: `docs/roadmap.md`, `BookToolsViewModel.executeAiGeneration`.

---

## 0. Prerequisites

- A working local Ollama server, plus at least one cloud key (Groq or Gemini are free-tier friendly).
- Set the provider in Settings → AI provider before each block.

## 1. Prompt-deck routing — the bug this fixed

Previously any non-Ollama provider was misrouted to `…/api/generate` and failed. These confirm the fix.

| # | Steps | Expected | ✅ |
|---|---|---|---|
| 1.1 | Provider = **Ollama**. Open a prompt deck, run a prompt. | Streams token-by-token as before; no regression. | ☐ |
| 1.2 | Provider = **Groq**. Run a prompt with variables filled. | Returns a real completion from Groq (arrives in one block, not streamed). No `/api/generate` error. | ☐ |
| 1.3 | Provider = **Gemini**. Run a prompt. | Real Gemini completion. | ☐ |
| 1.4 | Provider = **DeepSeek** (or any cloud). Run a prompt. | Real completion; the error banner does **not** say "connection failed / ensure X is reachable". | ☐ |
| 1.5 | Cloud provider with a **wrong API key**. Run. | Clean error surfaced (auth failed), not a silent hang. | ☐ |
| 1.6 | Prompt with an **attached image** + a vision-capable cloud model. Run. | Image is sent (base64); model responds about the image. | ☐ |
| 1.7 | Prompt with an attached image on **Ollama**. Run. | Unchanged from before this change. | ☐ |
| 1.8 | Start a cloud run, then hit **Stop**. | Generation cancels cleanly; UI returns to idle. | ☐ |

## 2. Privacy consent — prompt deck (regression) + MCQ (new)

Consent is one-time and global. To re-test, clear app data or toggle `ai_privacy_notice_shown`.

| # | Steps | Expected | ✅ |
|---|---|---|---|
| 2.1 | Fresh install (consent not yet given). Provider = cloud. Run a **prompt**. | Consent dialog "Data will be sent to {provider}" appears **before** any network call. | ☐ |
| 2.2 | Fresh install. Provider = cloud. Run the **MCQ generator**. | Consent dialog now appears here too (previously it did **not** — this is the new gate). | ☐ |
| 2.3 | Tap **Cancel** on the consent dialog. | No network call; nothing sent; run does not start. | ☐ |
| 2.4 | Tap **Send**. | Run proceeds; consent is remembered for subsequent runs. | ☐ |
| 2.5 | Provider = **Ollama**. Run prompt and MCQ. | No consent dialog (local, nothing leaves the device). | ☐ |

## 3. Network security — cloud HTTPS + no user CAs

| # | Steps | Expected | ✅ |
|---|---|---|---|
| 3.1 | **Release build.** Run a cloud provider (HTTPS). | Works normally. | ☐ |
| 3.2 | **Release build**, install a user CA + a MITM proxy (Charles/mitmproxy), point at a cloud call. | The call **fails** — user CAs are no longer trusted in release. This is the hardening working. | ☐ |
| 3.3 | **Debug build**, same proxy setup. | Works — debug-overrides still trust user CAs for tooling. | ☐ |
| 3.4 | Ollama on **emulator** (`10.0.2.2:11434`) over http. | Works. | ☐ |
| 3.5 | Ollama on **localhost/127.0.0.1** over http. | Works. | ☐ |
| 3.6 | ⚠️ Ollama on a **physical device pointing at a LAN IP** (e.g. `192.168.1.x`) over http, **release build**. | **Blocked** unless that IP is added to `network_security_config.xml`. Expected tradeoff — see the file's header comment. If you rely on this, add a `<domain>` line for your server. | ☐ |

## Known limitations

- **Cloud responses are not streamed.** `AiClient.chatComplete` is request/response, so a cloud run
  shows a spinner then the full text at once, whereas Ollama still streams. Acceptable — a working
  buffered response beats a broken streamed one. Adding SSE streaming to `AiClient` is future work.
- **Consent is global, not per-provider.** Once granted for any cloud provider it is not re-asked
  when switching to a different cloud provider. Tracked separately.
- **The routing branch is not unit-tested** — `feature:ui` has no test harness. It is a single
  `providerId.startsWith("ollama")` branch mirroring the already-tested `SettingsViewModel.pingProvider`.

*Status: Unverified on device | Created: 2026-07-21*
