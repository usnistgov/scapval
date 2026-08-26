# Release notes

- SCAP 1.4: `RES-181-1` (an `oval_system_characteristics` containing `system_data` but no `collected_objects`) is now reported as a warning (previously a validation error).
- SCAP 1.3 and 1.4: an `<xccdf:status>` value that is a valid XCCDF status value but not `draft`/`accepted` (`incomplete`, `interim`, `deprecated`) is now reported as a warning (`SRC-5-3`) noting the deviation from SP 800-126 Section 3.2.2. Previously this failed validation (1.3) or passed silently (1.4). Genuinely invalid status values remain an error (`SRC-5-1`).
