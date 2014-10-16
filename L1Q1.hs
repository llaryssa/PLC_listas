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
folhasdepoisdeInsetos fi [] = fi
folhasdepoisdeInsetos fi (((fd,(fmin,fmax)):xs)
											|(fmin<=fi&&fmax>=fi)  = folhasdepoisdeInsetos (fi - fd) xs
											|otherwise = folhasdepoisdeInsetos fi xs
folhasInsetos dias  planta [] = fi + dias*fd
folhasInsetos dias  (fi, fd) insetos 
						| (dias == 0) = fi
						| otherwise = folhasInsetos (dias-1) (folhasdepoisdeInsetos ((fi + fd) insetos ),fd) insetos

folhaspulverizadas (fi, fd) dias = fi + fd*dias

q1 vv vp [] li dc = []
q1 vv vp (p:ps) insetos dc 
		|((folhaspulverizadas p dc)*vv -vp)< vv*(folhasInsetos dc p li) = p:(q1 vv vp ps insetos dc)		
		|otherwise = (q1 vv vp ps insetos dc)		