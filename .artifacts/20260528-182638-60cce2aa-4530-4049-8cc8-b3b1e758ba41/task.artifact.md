# Task Management

- [/] Investigate spreadsheet parsing regression
	- [x] Examine `SpreadsheetHeaderMapper.kt`
	- [x] Examine `CompilerViewModel.kt`
	- [x] Examine `XlsxLibraryCompiler.kt`
	- [x] Document improved scoring and aliases in research artifact
- [ ] Implement enhanced spreadsheet parsing
	- [ ] Create `SpreadsheetHeaderMapperTest.kt` to reproduce the issue
	- [ ] Refactor `SpreadsheetHeaderMapper.kt` with weighted scoring and stricter matching
	- [ ] Update `CompilerViewModel.kt` to use improved scoring and scan limit
	- [ ] Update `XlsxLibraryCompiler.kt` with synchronized logic
- [ ] Verify the fix
	- [ ] Run `SpreadsheetHeaderMapperTest.kt`
	- [ ] Verify with simulated noisy data
