# OVAL 5.12.3 Schema Bundle — Provenance

These are the official OVAL Language 5.12.3 schemas, vendored locally so SCAPVal validates
OVAL 5.12.3 content offline (no network access at runtime).

## Source

- Project: OVAL Community — https://github.com/OVAL-Community/OVAL
- Release: `v5.12.3` — https://github.com/OVAL-Community/OVAL/releases/tag/v5.12.3
- Release published: 2026-06-06
- Schema version / date (from `oval-common-schema.xsd`): 5.12.3, 06/04/2026
- Release asset: `schemas-5.12.3.zip`
- Asset SHA-256: `a66fc02988a3b6bb3cad559e56ac4b47128b9d521316c182dd4e3145ebb8e5e8`

## Retrieval

Downloaded 2026-07-06 via the GitHub CLI:

    gh release download v5.12.3 --repo OVAL-Community/OVAL --pattern "schemas-5.12.3.zip"

Extracted verbatim into this directory: 54 `.xsd` files, official bytes unmodified.

## Integrity

Per-file SHA-256 checksums are recorded in `SHA256SUMS` (54 entries). Verify with:

    shasum -a 256 -c SHA256SUMS

Anchor hashes:

- `oval-common-schema.xsd`       `678824ffa43a26ec6a7c49b2f9536dbc85be2a94627d9835e4cc65afdd827df2`
- `oval-definitions-schema.xsd`  `06b79e8683d6b9dcaad3ab074a456588ed82c4edd2e8fff209def10aa0a366cc`

## Notes

- 53 of 54 files differ from the vendored `oval_5.12.2` bundle; only `xmldsig-core-schema.xsd`
  (the external W3C XML-DSig schema) is byte-identical, as expected.
- Files are unmodified from the official release. Do not hand-edit. To update, re-vendor from the
  authoritative release and regenerate `SHA256SUMS`.
- Runtime resolution is handled by `scapval-catalog.xml` (OVAL 5.12.3 rewrite entries) and the
  classpath resource resolver; validation requires no network access.
- `PROVENANCE.md` and `SHA256SUMS` are ignored by the runtime schema loader, which only loads
  files ending in `.xsd`.
