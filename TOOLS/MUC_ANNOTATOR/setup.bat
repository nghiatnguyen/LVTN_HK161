@ECHO OFF
setlocal EnableDelayedExpansion
SET batfilename=MUCAnnotate.bat
SET curent_path=%~dp0
SET head=java -jar "
SET tail=dist\DataPreprocessing.jar" "%%1"
SET batfilepath=%curent_path%%batfilename%
SET batfilepath=%batfilepath:\=\\%
SET mucannotate_cmd=%head%%curent_path%%tail%
echo %mucannotate_cmd%>MUCAnnotate.bat

SET text_reg_file=Windows Registry Editor Version 5.00 ^

; the extension .abc gets associated with a file type^

[HKEY_CLASSES_ROOT\.mdt]^

@="muc-data"^

; the file-type gets a name (that appears in explorer in field "type")^

[HKEY_CLASSES_ROOT\muc-data]^

@="muc data file"^

; What will appear in the contextual menu when selecting an .abc file^

[HKEY_CLASSES_ROOT\muc-data\shell\annotate]^

@="Annotate in MUC"^

; What to do with it^

; here, %1 is the file given as argument of the script^

[HKEY_CLASSES_ROOT\muc-data\shell\annotate\command]^

@="\"%batfilepath%\" \"%%1\""^

[HKEY_CLASSES_ROOT\Directory\shell\annotate]^

@="Annotate its mdt files in MUC"^

[HKEY_CLASSES_ROOT\Directory\shell\annotate\command]^

@="\"%batfilepath%\" \"%%1\""
echo !text_reg_file!>config.reg

call config.reg
del config.reg