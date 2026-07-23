# PRD — Artier IDE Lite (Kotlin Native, Referensi Fitur Void IDE)

**Product:** Artier IDE Lite
**Tagline:** *Agentic AI First — Lite*
**Platform:** Android (tablet), target device **Xiaomi/Redmi Pad SE 11" (RAM 4GB)**
**Varian arsitektur:** Kotlin native (Jetpack Compose) + Sora Editor + Termux TerminalView, fitur AI mengadopsi pola **Void IDE** (voideditor/void)
**Versi dokumen:** v2.0 — rewrite total dari `artier-ide-lite-kotlin-prd.md`, referensi fitur Void
**Status:** Perencanaan awal — menggantikan draft sebelumnya sebagai baseline utama

> ⚠️ **Catatan status Void**: per riset terakhir, pengembangan Void IDE **di-pause** oleh tim mereka ("paused to explore a few novel coding ideas... might not resume Void as an IDE"). Artier **tidak bergantung** pada Void tetap aktif — kita ambil snapshot pola fitur & UX mereka sebagai referensi desain matang (mereka sendiri fork dari VS Code, battle-tested), lalu implementasi ulang 100% native untuk Android. Tidak ada dependency kode ke repo Void sama sekali.

---

## 0. Kenapa Void Jadi Referensi, Bukan CLI Adapter Registry

Pergeseran filosofi dari draft v1 (`artier-ide-lite-kotlin-prd.md`): sebelumnya AI Assistant dirancang mengikuti pola **OpenCode** (agent `build`/`plan` biner + subagent terpisah). Void menawarkan **model interaksi yang lebih matang untuk kerja sehari-hari** karena sudah teruji jutaan user sebagai editor, bukan cuma CLI: granularitas mode lebih halus (3 mode, bukan 2), plus fitur **level-editor** yang tidak dimiliki CLI agent manapun (autocomplete, quick edit inline, checkpoint visual).

### 0.1 Tabel Mapping: Fitur Void → Keputusan di Artier

| Fitur Void | Status di Artier IDE Lite | Alasan |
|---|---|---|
| Agent Mode / Gather Mode / Normal Chat | ✅ **Diadopsi, menggantikan `build`/`plan`** | Lebih granular — Gather Mode mengisi celah "eksplorasi read-only tanpa commit ke edit", yang di desain lama cuma didekati approximate oleh `plan` |
| Tab Autocomplete | ✅ **Baru diimplementasikan** | Belum ada di desain manapun sebelumnya — celah besar untuk IDE yang mengklaim "Agentic AI First" |
| Quick Edit (inline diff) | ✅ **Baru diimplementasikan** | Interaksi paling sering dipakai sehari-hari di Cursor/Void, belum ada di draft manapun |
| Checkpoints | ✅ **Baru diimplementasikan, menggantikan andalan git manual** | Rollback cepat tanpa keluar dari flow AI |
| Fast Apply vs Slow Apply | ✅ **Baru diimplementasikan** | Search-replace bertarget vs full regenerate — pilihan berbasis ukuran file |
| Lint auto-fix loop | ✅ **Baru diimplementasikan** | Agent Mode otomatis perbaiki lint error hasil editnya sendiri |
| Model capability auto-detect | ✅ **Baru diimplementasikan** | Auto-deteksi model support tools/FIM/reasoning — penting karena Artier multi-provider (NaraRouter/9Router/BYOK) |
| MCP native | ✅ **Sudah ada di v1** (§5.2 draft lama), **diperkuat** jadi first-class citizen | Konsisten dengan pola Void: bukan fitur tempelan |
| Import setting VS Code (1-klik) | ⚠️ **Diadaptasi terbatas** — bukan transfer VS Code (tidak relevan mobile), tapi **import `.vscode/settings.json` project** untuk baca preferensi format/linter project yang sudah ada | VS Code settings umum ditemukan di banyak repo, berguna auto-konfigurasi |
| SSH/WSL remote support | ✅ **Sudah dibahas terpisah** (deployment remote via Tailscale, lihat §9) | Relevan, tidak perlu diulang detail di sini |
| Direct-to-provider, no backend/telemetry | ✅ **Prinsip yang sudah dianut sejak awal** (BYOK, API key di Keystore) | Selaras dengan filosofi "own the loop" kita |
| OpenCode & Claude Code adapter (dari revisi sebelumnya) | ✅ **Tetap dipertahankan** sebagai opsi "Sumber AI" | Tidak digantikan Void — Void tidak API-compatible sebagai CLI agent, jadi keduanya melengkapi, bukan tumpang tindih |

---

## 0.2 Target Device Resmi: Xiaomi/Redmi Pad SE 11" (dipertahankan dari draft v1)

| Spesifikasi | Detail |
|---|---|
| Layar | 11.0" IPS LCD, 1920×1200 (FHD+), 90Hz, 400 nits |
| Chipset | Snapdragon 680 4G (SM6225, 6nm) |
| CPU | 4× 2.4GHz Kryo 265 Gold + 4× 1.9GHz Kryo 265 Silver |
| GPU | Adreno 610 |
| RAM | 4GB LPDDR4X (target kondisi terburuk) |
| Storage | 128GB **eMMC 5.1** (bukan UFS — I/O lebih lambat) |
| OS | Android 13 → HyperOS 2/Android 15 |
| Konektivitas | Wi-Fi only |

**Implikasi tambahan khusus fitur baru di dokumen ini:**
- **Tab Autocomplete butuh latensi rendah** — di device CPU kelas menengah-bawah ini, autocomplete **wajib** pakai model kecil/cepat (bukan model reasoning besar) dan **debounce ketat** (jangan trigger request tiap keystroke, tunggu jeda ~150-250ms + hanya saat cursor berhenti).
- **Checkpoint storage** (snapshot file sebelum tiap edit) menambah beban I/O ke eMMC — perlu strategi diff-based storage (simpan delta, bukan full-copy tiap file) supaya tidak memperberat storage yang sudah lebih lambat dari UFS.

---

## 1. Ringkasan Produk

Artier IDE Lite adalah IDE native Android yang mengadopsi **model interaksi AI ala Void/Cursor** (Tab Autocomplete, Quick Edit, Agent/Gather/Chat mode, Checkpoint) — diimplementasikan ulang 100% dari nol dengan Kotlin + TypeScript daemon, bukan port/fork Void (yang berbasis Electron dan tidak mungkin jalan di tablet 4GB).

---

## 2. Goals & Non-Goals

### Goals (v1)
- App native Android (Sora Editor + TerminalView, tanpa WebView untuk fitur inti), stabil di Redmi Pad SE 4GB.
- **3 mode interaksi AI** (Agent / Gather / Chat) menggantikan model `build`/`plan` sebelumnya.
- **Tab Autocomplete** — completion inline level-token, model kecil/cepat khusus.
- **Quick Edit** — select kode, describe perubahan, terima diff inline.
- **Checkpoint system** — snapshot otomatis sebelum AI edit, rollback 1-tap.
- **Fast Apply / Slow Apply** — strategi apply otomatis dipilih berdasarkan ukuran perubahan.
- **Lint auto-fix loop** di Agent Mode.
- **Model capability auto-detect** (tools/FIM/reasoning) per provider yang dikonfigurasi.
- MCP native (client & server, dari draft v1).
- 2 CLI adapter opsional (OpenCode, Claude Code) tetap ada sebagai "Sumber AI" alternatif.
- Extension system (`.artx`), skill system (`SKILL.md`, default dari `anthropics/skills`), public URL tunnel — **dipertahankan dari draft v1, tidak berubah**.

### Non-Goals (v1)
- ❌ Tidak fork/port kode Void secara langsung (Electron, tidak cocok mobile).
- ❌ Tidak bergantung pada Void tetap dikembangkan aktif.
- ❌ Tidak ada Workspace Canvas, tidak ada panel Database remote (sama seperti draft v1).
- ❌ Bukan cross-platform (Android-only).

---

## 3. Fitur Baru — Detail Implementasi

### 3.1 Tab Autocomplete

**Alur kerja:**
1. User mengetik di Sora Editor.
2. Setelah jeda ~150-250ms tanpa keystroke baru (debounce), daemon kirim **FIM request** (Fill-In-the-Middle: prefix + suffix di sekitar cursor) ke model.
3. Model kecil/cepat (lihat rekomendasi di §3.1.1) mengembalikan saran completion.
4. Saran ditampilkan sebagai **ghost text** (teks abu-abu transparan) tepat di posisi cursor — Sora Editor mendukung custom rendering overlay untuk ini.
5. Tekan **Tab** → teks diterima. Ketik karakter lain / tekan Esc → saran dibatalkan.

**3.1.1 Model untuk Autocomplete — Perlu Model Terpisah dari Chat**

Berbeda dari Agent/Gather/Chat (yang butuh model besar untuk reasoning), autocomplete butuh **model kecil khusus FIM** supaya terasa instan:
- Rekomendasi: model kelas 1-3B parameter dengan FIM capability (mis. keluarga Qwen Coder kecil) — via NaraRouter/9Router/Ollama Cloud.
- **Setting terpisah** di menu AI Providers: "Model untuk Autocomplete" vs "Model untuk Chat/Agent" — supaya user bisa pilih model beda untuk masing-masing kebutuhan (persis pola Void).
- Kalau device benar-benar terbatas dan latency tetap terasa lambat meski model kecil (jaringan lambat, dsb), sediakan **toggle disable Tab Autocomplete** di Settings — supaya user bisa matikan kalau lebih mengganggu daripada membantu.

### 3.2 Quick Edit (Inline Diff)

**Alur kerja:**
1. User select rentang kode di Sora Editor (highlight, seperti select-copy biasa).
2. Muncul **floating action button kecil** di dekat selection (mis. ikon ✏️) atau lewat command palette/keybinding.
3. User ketik instruksi singkat ("ubah jadi async/await", "tambah error handling").
4. Daemon kirim request ke model (pakai model Chat/Agent, bukan model autocomplete) dengan konteks: kode terselect + sedikit konteks sekitarnya.
5. Hasil ditampilkan sebagai **inline diff** — baris lama (strikethrough merah) & baris baru (hijau) langsung di posisi kode aslinya, bukan di panel terpisah.
6. Tombol **Accept** / **Reject** / **Accept & Continue** (untuk instruksi lanjutan).

**Implementasi native**: Sora Editor mendukung custom span/decoration — diff rendering dibuat sebagai overlay `Span` dengan warna background berbeda per baris (mirip cara syntax highlighting bekerja), bukan replace teks langsung sebelum di-Accept.

### 3.3 Chat Mode: Agent / Gather / Chat (Menggantikan Build/Plan)

Menggantikan desain biner lama, sekarang **3 mode** dipilih dari dropdown yang sama posisinya dengan dropdown "Sumber AI" sebelumnya:

| Mode | Permission | Kapan dipakai |
|---|---|---|
| **Agent** | `read: allow`, `edit: allow`, `bash: allow`, MCP tools: `allow` | Kerja development penuh — setara agent `build` versi lama, tapi dengan tambahan MCP native & lint auto-fix |
| **Gather** | `read: allow`, `edit: deny`, `bash: deny`, MCP tools (read-only saja): `allow` | Eksplorasi codebase, tanya-jawab tanpa risiko perubahan — **lebih jelas batasannya** dibanding `plan` versi lama yang kadang ambigu soal "boleh saran tapi gimana batasnya" |
| **Chat** | Tidak ada tool sama sekali | Ngobrol soal konsep, tanya general programming, tanpa akses codebase — mode paling ringan dan cepat |

**Catatan penting**: mode `plan` versi lama secara konsep **digantikan sepenuhnya oleh Gather Mode** — sama-sama read-only, tapi Gather Mode Void punya kejelasan UX lebih baik (dipisah dari kemampuan "kasih saran perubahan" yang di `plan` versi lama agak bercampur).

### 3.4 Checkpoint System (Menggantikan Ketergantungan ke Git Manual)

**Alur kerja:**
1. Setiap kali Agent Mode akan melakukan edit file, daemon **otomatis snapshot** state file sebelum edit (bukan full-copy — simpan sebagai **diff/patch terhadap versi sebelumnya**, format mirip git diff, untuk hemat storage eMMC).
2. Checkpoint muncul sebagai **timeline visual** di panel AI Assistant — tiap titik checkpoint bisa diklik.
3. Klik checkpoint lama → preview perubahan yang terjadi sejak titik itu → tombol **"Restore to this point"**.
4. Checkpoint **tidak menggantikan git** — ini untuk rollback cepat dalam sesi kerja aktif (granularitas per-AI-edit), sementara git tetap dipakai untuk versioning formal/commit history.

**Penyimpanan**: SQLite tabel `checkpoints` (project_id, file_path, diff_patch, timestamp, ai_action_description) — diff-based supaya tidak membengkak, penting mengingat storage eMMC yang lebih lambat.

### 3.5 Fast Apply vs Slow Apply

| Strategi | Kapan dipakai | Cara kerja |
|---|---|---|
| **Fast Apply** | File besar, perubahan kecil/lokal | Model hasilkan **search-replace block** (bagian lama → bagian baru), daemon eksekusi replace bertarget di file asli — tidak perlu regenerasi seluruh file |
| **Slow Apply** | File kecil, atau perubahan menyebar di banyak bagian | Model regenerasi **seluruh isi file** dari awal, daemon replace penuh |

**Keputusan otomatis**: daemon pilih strategi berdasarkan ukuran file (threshold, mis. >200 baris → cenderung Fast Apply) dan cakupan perubahan yang diminta model — user bisa override manual dari Settings kalau mau selalu satu strategi.

### 3.6 Lint Auto-Fix Loop (Agent Mode saja)

Setelah Agent Mode selesai edit file, daemon otomatis:
1. Jalankan linter yang terdeteksi di project (ESLint, ktlint, dsb — deteksi dari config file project) via tool `bash`.
2. Kalau ada error/warning baru yang muncul akibat edit tadi, **kirim balik ke agent loop** sebagai konteks tambahan ("hasil edit menyebabkan lint error berikut, perbaiki") — 1 iterasi tambahan otomatis, dibatasi max 1-2 kali retry supaya tidak infinite loop.
3. User bisa lihat log lint-fix ini di panel AI Assistant sebagai langkah terpisah ("🔍 Checking lint... → 🔧 Fixing 2 issues...").

### 3.7 Model Capability Auto-Detect

Karena Artier multi-provider (NaraRouter, 9Router, BYOK langsung), tiap model punya kapabilitas beda. Daemon melakukan **probe otomatis** saat provider baru ditambahkan di Settings:
- **Tool-calling support?** — kirim test request dengan 1 tool dummy, cek apakah model merespons dengan tool_call yang valid.
- **FIM support?** — cek dokumentasi/pola endpoint (beberapa provider expose endpoint completion terpisah dari chat), atau uji langsung dengan prompt format FIM standar.
- **Reasoning/thinking support?** — cek response format (apakah ada `thinking`/`reasoning_content` block).

Hasil probe disimpan sebagai metadata model di SQLite, dipakai untuk:
- **Sembunyikan mode yang tidak didukung** — kalau model dipilih tidak support tool-calling, Agent/Gather Mode otomatis di-disable untuk model itu (cuma Chat Mode yang tersedia), mencegah user bingung kenapa Agent Mode "tidak jalan".
- **Rekomendasi model untuk Autocomplete** — filter otomatis ke model yang FIM-capable saja di dropdown pemilihan.

### 3.8 Import Konteks dari `.vscode/settings.json` Project

Bukan transfer setting aplikasi (tidak relevan lintas platform mobile↔desktop), tapi **baca preferensi format dari project** yang sudah ada:
- Kalau project punya `.vscode/settings.json` (umum ditemukan di banyak repo publik), daemon baca `editor.tabSize`, `editor.formatOnSave`, `[language].defaultFormatter`, dsb — otomatis diterapkan ke Sora Editor untuk project itu.
- Berguna terutama untuk project yang di-clone dari GitHub yang sudah punya konvensi tim.

---

## 4. Arsitektur Tingkat Tinggi (Update)

```
┌───────────────────────────────────────────────────────────────┐
│  Artier IDE Lite — Android App (Kotlin, Jetpack Compose)         │
│  ┌──────────────┐              ┌─────────────────────────────┐ │
│  │ Sidebar       │              │ AI Assistant Panel (Compose)  │ │
│  │ 🏠🤖🧩🧠⚙      │              │ Sumber: [Built-in/OpenCode/  │ │
│  └──────────────┘              │          Claude Code ▾]       │ │
│                                  │ Mode: [Agent/Gather/Chat ▾]   │ │
│                                  │ Checkpoint timeline            │ │
│                                  └─────────────────────────────┘ │
│  ┌───────────────────────────────────────────────────────────┐ │
│  │ Sora Editor (native)                                          │ │
│  │  + Ghost text overlay (Tab Autocomplete)                      │ │
│  │  + Inline diff overlay (Quick Edit)                            │ │
│  │ TerminalView + TerminalEmulator (Termux, native)                │ │
│  └───────────────────────────────────────────────────────────┘ │
└──────────────────────────┬──────────────────────────────────────┘
                            │ OkHttp / WebSocket
┌──────────────────────────▼──────────────────────────────────────┐
│  Daemon (Node.js + TypeScript + Fastify, di proot)                 │
│  ┌────────────────────────────────────────────────────────────┐ │
│  │ Artier Agent Loop ("own the loop")                             │ │
│  │  - Mode handler: Agent / Gather / Chat (permission per-mode)   │ │
│  │  - Autocomplete service (FIM, model terpisah, debounced)        │ │
│  │  - Quick Edit service (diff generation)                         │ │
│  │  - Checkpoint manager (diff-based snapshot, SQLite)              │ │
│  │  - Apply strategist (Fast/Slow Apply decision)                   │ │
│  │  - Lint runner + auto-fix loop                                   │ │
│  │  - Model capability prober                                        │ │
│  │  - Vercel AI SDK Core (streamText + tool()) → 1x/iterasi          │ │
│  ├────────────────────────────────────────────────────────────┤ │
│  │ Agent Adapter Registry: OpenCode, Claude Code (opsional)         │ │
│  ├────────────────────────────────────────────────────────────┤ │
│  │ Skill loader · Extension host · Tunnel manager (cloudflared)    │ │
│  └────────────────────────────────────────────────────────────┘ │
└───────────────────────────────────────────────────────────────┘
                            │ HTTPS
┌──────────────────────────▼──────────────────────────────────────┐
│  NaraRouter (default) / 9Router (opsional) / BYOK langsung          │
└───────────────────────────────────────────────────────────────┘
```

---

## 5. Wireframe — Panel AI Assistant dengan Fitur Baru

```
┌───────────────────────────────┐
│ AI Assistant              [✕] │
│ Sumber: [Built-in Agent    ▾] │
│ Mode:   [🤖 Agent          ▾] │
├───────────────────────────────┤
│ 📍 Checkpoint Timeline          │
│  ● 14:32 Edit auth.controller  │
│  ● 14:30 Edit user.service     │
│  ○ 14:28 (awal sesi)           │
│  [↺ Restore ke titik ini]       │
├───────────────────────────────┤
│ 🔍 Checking lint...             │
│ 🔧 Fixed 2 issues in auth.ts    │
│ "Sudah saya perbaiki validasi   │
│  email dan tambah error         │
│  handling untuk kasus null"     │
├───────────────────────────────┤
│ [Ask anything...          ] [➤]│
└───────────────────────────────┘
```

```
┌─────────────────────────────────────────────┐
│  12  const handleLogin = async (email) => {  │
│  13    if (!email) return null              │  ← merah, strikethrough (lama)
│  13    if (!email) throw new ValidationError │  ← hijau (baru, Quick Edit)
│  14    ...                                    │
│                             [✓ Accept] [✕ Reject] │  ← inline, tepat di posisi edit
└─────────────────────────────────────────────┘
```

```
┌─────────────────────────────────────────────┐
│  const result = users.filter(u => u.act|     │  ← cursor di sini
│                                    ive)       │  ← ghost text abu-abu (Tab Autocomplete)
│                                     [Tab ↹]    │
└─────────────────────────────────────────────┘
```

---

## 6. Tech Stack Tambahan (Delta dari `artier-ide-lite-kotlin-prd.md`)

| Komponen | Pilihan | Catatan |
|---|---|---|
| Ghost text rendering | Custom `Span`/decoration di atas Sora Editor | Perlu extend Sora Editor API untuk overlay non-destructive |
| Inline diff rendering | Custom line-background span (mirip syntax highlight layer) | Tidak replace teks sampai user Accept |
| Debounce autocomplete | Kotlin Coroutine `debounce` operator (Flow) di sisi Compose/ViewModel | Kontrol jeda sebelum request dikirim ke daemon |
| Checkpoint storage | SQLite (`better-sqlite3`), diff-based (bukan full snapshot) | Hemat I/O eMMC |
| Lint detection | Deteksi config file project (`.eslintrc*`, `ktlint`, dll) → jalankan via tool `bash` di daemon | Reuse tool `bash` yang sudah ada di agent loop |
| Model capability probe | Custom TypeScript, test-request saat provider ditambahkan | Hasil disimpan SQLite, dipakai UI untuk filter mode/model |

---

## 7. Fitur (Scope per Fase) — Update dari Draft v1

### Fase 0-3 — Sama seperti draft v1
Fondasi, Editor & Terminal, Public Tunnel, Model Gateway (NaraRouter) — tidak berubah.

### Fase 4 — Agent Loop Inti + 3 Mode (Update)
- `core/agent-loop.ts` (own-the-loop) + Vercel AI SDK Core.
- Tool dasar: `read`, `write`, `edit`, `bash`, `glob`, `grep`.
- **3 mode**: Agent, Gather, Chat (menggantikan `build`/`plan`).
- Permission engine per-mode.

### Fase 5 — Subagent & Skill
Sama seperti draft v1 (tool `task`, `skill`, bootstrap skill default dari `anthropics/skills`).

### Fase 5.5 — Tab Autocomplete & Quick Edit *(baru)*
- FIM service + debounce + ghost text overlay.
- Quick Edit service + inline diff overlay + Accept/Reject flow.
- Setting model terpisah untuk autocomplete.

### Fase 5.8 — Checkpoint & Apply Strategy *(baru)*
- Checkpoint manager (diff-based) + timeline UI.
- Fast Apply / Slow Apply strategist.
- Lint auto-fix loop.
- Model capability prober.

### Fase 6 — Extension System
Sama seperti draft v1.

### Fase 6.5 — CLI Adapter (OpenCode, Claude Code) & Settings Lengkap
Sama seperti draft v1 — tetap dipertahankan sebagai opsi "Sumber AI".

### Fase 6.8 — Dockable Panel System
Sama seperti draft v1 (§6.4 dokumen sebelumnya) — tidak berubah.

### Fase 7 — Polish & Optimisasi RAM
Termasuk tuning khusus latency autocomplete di device Snapdragon 680.

---

## 8. Prinsip Manajemen Performa Khusus Fitur Baru

1. **Autocomplete tidak boleh blocking UI thread** — request async penuh, kalau response belum datang saat user sudah ketik karakter berikutnya, **request lama dibatalkan** (cancel token), jangan biarkan response usang muncul terlambat.
2. **Checkpoint diff generation di background thread** — jangan hitung diff di main thread, terutama untuk file besar.
3. **Ghost text tidak memicu re-layout penuh** editor — pakai overlay/decoration layer terpisah dari teks asli, supaya render-nya murah (tidak trigger reflow seluruh dokumen).
4. **Lint auto-fix dibatasi 1-2 iterasi** — cegah loop tak berkesudahan yang boros token & waktu.
5. **Model capability probe di-cache** — jangan re-probe tiap kali dipakai, cukup saat provider pertama ditambahkan atau user klik "Re-check capability" manual.

---

## 9. Kompatibilitas dengan Deployment Remote (Sama seperti Draft v1)

Kalau daemon dijalankan remote (mini PC via Tailscale) alih-alih proot lokal — semua fitur baru di dokumen ini (autocomplete, quick edit, checkpoint, lint loop) **otomatis ikut pindah** karena semuanya logic di sisi daemon. Satu catatan tambahan: **autocomplete jadi fitur paling sensitif terhadap latency remote** — kalau round-trip ke mini PC terasa lambat, pertimbangkan cache lokal ringan di app (predictive local buffering) atau nonaktifkan autocomplete khusus saat mode remote dan pertahankan Quick Edit/Agent Mode saja (yang secara natural lebih toleran latency karena sifatnya bukan real-time per-keystroke).

---

## 10. Pertanyaan Terbuka untuk Revisi

1. **Prioritas implementasi** — dari semua fitur baru (Autocomplete, Quick Edit, Checkpoint, Fast/Slow Apply, Lint loop, Capability probe), mana yang paling ingin kamu lihat jalan duluan? Autocomplete & Checkpoint kemungkinan paling kompleks secara teknis.
2. **Autocomplete: wajib aktif atau opt-in?** — mengingat constraint device, apakah defaultnya ON (dengan toggle mudah untuk matikan) atau OFF (user aktifkan manual kalau mau coba)?
3. Semua pertanyaan terbuka dari draft v1 (§9 dokumen `artier-ide-lite-kotlin-prd.md`) — soal NaraCLI, command palette, extension marketplace, dockable panel — **masih berlaku**, dokumen ini tidak menjawabnya, hanya menambah lapisan fitur AI baru di atasnya.
