-- L1PLC Q1
{-
vv = valor de venda
vp = valor de pulverização
lp = lista de plantas[(fi, fd)]
	fi = folhas iniciais
	fd = folhas nascidas por dia
li = lista de insetos [(fd, (f1, fd))]
	fd = folhas comidas por dia, 
	fmin = so gosta d plancas com no minimo essa quantidade de folhas
	fmax = so gosta de plantas com no maximo essa quantidade de folhas
dc = dias até a colheita
-}
{-
	problema principal:
		decidir o q vale mais:
			pulverizar e vender as folhas salvas
				= (fi+folhasgeradas)*valorVenda - vp
			nao pulverizar e vender as folhas
				= (fi+folhasgeradas-folhascomidas)*valorvenda


-}
-- fi insetos -> inteiro
folhasdepoisdeInsetos::Int->[(Int,(Int,Int))]->Int
folhasdepoisdeInsetos fi [] = fi
folhasdepoisdeInsetos fi (((fd,(fmin,fmax)):xs))
											|(fmin<=fi&&fmax>=fi)  = folhasdepoisdeInsetos (fi - fd) xs
											|otherwise = folhasdepoisdeInsetos fi xs

folhasInsetos:: Int->(Int,Int)->[(Int,(Int,Int))]->Int
folhasInsetos dias  (fi, fd) [] = fi + dias*fd
folhasInsetos dias  (fi, fd) insetos 
						| (dias == 0) = fi
						| otherwise = folhasInsetos (dias-1) ((folhasdepoisdeInsetos (fi + fd) insetos ),fd) insetos


folhaspulverizadas:: (Int, Int) ->Int-> Int
folhaspulverizadas (fi, fd) dias = fi + fd*dias


q1:: Int->Int->[(Int,Int)]->[(Int,(Int,Int))]->Int->[(Int,Int)]
q1 vv vp [] li dc = []
q1 vv vp (p:ps) li dc 
		|((folhaspulverizadas p dc)*vv -vp) >= vv*(folhasInsetos dc p li) = p:(q1 vv vp ps li dc)		
		|otherwise = (q1 vv vp ps li dc)



valorvenda = 2
valorpulv = 12
plantas = [(5, 3), (15, 0), (10, 2), (5, 5), (0, 7)]
insetos = [(2, (2, 5)), (1, (12, 20)), (2, (5, 10)), (2, (3, 10)), (2, (10,20))]
dias = 2

resultado = q1 valorvenda valorpulv plantas insetos dias

{--
1. 8, 8, 8, 6, 4, 4 | 7, 7, 7, 5, 3, 3              6
2. 15, 15, 14, 14, 14, 12 | 12, 12, 11, 11, 11, 9   18
3. 12, 12, 11, 11, 11, 9 | 11, 11, 11, 11, 11, 9    18
4. 10, 10, 9, 7, 5, 5 | 10, 10, 10, 8, 6, 6         12          
5. 7, 7, 7, 5, 3, 3 | 10, 10, 10, 8, 6, 6           12

1. 8, 11     22 - 12 = 10
2. 15, 15    30 - 12 = 18
3. 12, 14    28 - 12 = 16
4. 10, 15    30 - 12 = 18
5. 7 14      28 - 12 = 16

--}