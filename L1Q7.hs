checaOrdenacao :: [String] -> (String->String->Bool) -> ([String], Bool)
checaOrdenacao l f | l == ordenada = (ordenada, True)
				   | otherwise     = (ordenada, False)
				   where ordenada = sort l f

sort :: [String] -> (String->String->Bool) -> [String]
sort [] f = []
sort (a:as) f = sort [b | b <- as, (f a b)] f
					   ++ [a] ++
				sort [c | c <- as, not (f a c)] f

palavras = ["mandibula", "amor", "bola", "dor", "dragao", "manda", "xixi", "lilas", "vaso"]

resultado = checaOrdenacao palavras (>)