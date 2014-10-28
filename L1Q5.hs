gerarChaveDiffieHellman :: Integer -> Integer -> Integer -> Integer -> Integer
gerarChaveDiffieHellman p g a b = mod (aa ^ b) p where aa = mod (g ^ a) p 

resultado =  gerarChaveDiffieHellman 97 5 36 58