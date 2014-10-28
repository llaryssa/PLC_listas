func1 :: a -> String -> (String->Bool) -> (a, String)
func1 x w f | (f w)     = (x, w)
			| otherwise = (x, "")

func2 :: [(String->e)] -> [String] -> [(String->Bool)] -> [e]
func2 l1 l2 l3 = map aplicaFuncao comb
			  where comb = [(fparc x y) | fparc <- (map func1 l1), 
										  x <- l2, y <- l3]

aplicaFuncao :: (String->e, String) -> e
aplicaFuncao (f, a) = f a



--------------------------------------

funcTeste :: String -> Int
funcTeste [] = 0;
funcTeste (s:sx) = fromEnum s + funcTeste sx

funcTeste2 :: String -> Bool
funcTeste2 s = (s < "opa")

resultado = func2 [funcTeste, funcTeste] ["ola, oi"] [funcTeste2, funcTeste2]