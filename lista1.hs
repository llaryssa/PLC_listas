-- questão 4

gerarListaCombinacoes :: Int -> Int -> [[Int]]
gerarListaCombinacoes n k = gerarCombinacoes k lista (aPartirDeI (k-1) lista)
								where lista = (gerarListaZeroANmenos1 n)

gerarListaZeroANmenos1 :: Int -> [Int]
gerarListaZeroANmenos1 0 = [];
gerarListaZeroANmenos1 n = (gerarListaZeroANmenos1 (n-1)) ++ [n-1]

aPartirDeI :: Int -> [Int] -> [Int]
aPartirDeI 0 xs = xs
aPartirDeI i [] = []
aPartirDeI i (x:xs) = aPartirDeI (i-1) xs
-- tail quando a cabeca for i

gerarCombinacoes :: Int -> [Int] -> [Int] -> [[Int]]
gerarCombinacoes 0 xs t = [[]]
gerarCombinacoes n xs [] = []
gerarCombinacoes n (x:xs) t = map (x:) (gerarCombinacoes (n-1) xs t) 
								++ gerarCombinacoes n xs (aPartirDeI 1 t)
-- primeiro coloca x em todo mundo e roda até acabar os k numeros de cada combinacao
-- depois concatena isso com as listas sem x





-- questão 5

gerarChaveDiffieHellman :: Integer -> Integer -> Integer -> Integer -> Integer
gerarChaveDiffieHellman p g a b = mod (aa ^ b) p where aa = mod (g ^ a) p 
-- ex: gerarChaveDiffieHellman 97 5 36 58