checkPalavra [] s = True
checkPalavra s [] = False
checkPalavra (a:as) (b:bs) = if (a==b) || (abs(fromEnum a - fromEnum b) == 32) then 
								checkPalavra as bs 
							  else False

buscaPalavra w [] = []
buscaPalavra w (l:ls) = if (checkPalavra w l) then 
				l:(buscaPalavra w ls)
			 else buscaPalavra w ls


resultado = buscaPalavra "espor" ["Esportivo", "SPORT", "esPorte", "eSpIrItO", "EspOr"]