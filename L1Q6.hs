filtroGerarPares :: [(String, Char, Bool)] -> Char -> [(String)]
filtroGerarPares [] pref = []
filtroGerarPares ((nome, sexo, quer):xs) pref 
	| quer == False   = filtroGerarPares xs pref
	| sexo == pref    = (nome) : filtroGerarPares xs pref
	| pref == 'x'     = (nome) : filtroGerarPares xs pref
	| pref == 'v'     = filtroGerarPares ((nome, sexo, quer):xs) 'h'
	| otherwise       = filtroGerarPares xs pref

list = [("Glaucio", 'h', True), ("Zeno", 'h', False), ("Josiela", 'm', True), ("Juliana", 'm', False), ("Priscillo", 'm', True), ("Robson", 'm', False)]
pref = 'm'
resultado = filtroGerarPares list pref