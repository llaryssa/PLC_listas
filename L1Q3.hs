
-- n numero de buckets, 
-- lista de inteiros
--lista de funcoes
--lista de buckets
bucketSort:: Int->[Int]->[[Int]->[Int]]->[Int]
bucketSort n [] fs = []
bucketSort n list fs = oi (listarBuckets list n (maximum list) 1) fs fs


--recebe lista de inteiros, n, max, b e devolve buckets
listarBuckets:: [Int]->Int->Int->Int->[[Int]]
listarBuckets list n ma b | list==[] = []
						| b== (n+1) = []
						  |otherwise = [ x | x <- list, x >= ((div ma n) * (b-1)), x <= ((div ma n) * b)]:listarBuckets list n ma (b+1) 

--recebe a lista de buckets e retorna eles ordenados
oi:: [[Int]]->[[Int]->[Int]]->[[Int]->[Int]]->[Int]
oi  []    fs   d = []
oi 	 bks      []    [] = []
oi   bks      []     d = oi bks d d
oi (b:bs) (f:fs)   d = (f b)++(oi bs fs d)



