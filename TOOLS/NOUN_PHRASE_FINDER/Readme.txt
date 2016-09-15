REQUIREMENTS
Java is added in environment variables

STRUCTURE OF CODE:
In folder "DomParser", includes 3 classes:
	1) ReadXMLFile: Read file ouput of Stanford tools, get Noun Phrases and export to file NounPhrase.txt
	2) NounPhrase: Struct of each Noun Phrase: id, sentence id, text
	3) Checker: 
		1. check NP is pronoun
		2. check NP is definite
		3. check NP is demonstrative
		4. distance of 2 NPs
HOW TO RUN
Just double click to NP_Finder.jar, it will read input file "input.txt.out" and create output file "NounPhrases.txt"