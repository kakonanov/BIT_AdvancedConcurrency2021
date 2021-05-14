#Курсовая 1 - Quicksort
***
##Классы:

* ParallelUtils - содержит реализации функций parallelFor, blockedParallelFor, map, scan, filter
* SerialUtils - их последовальные аналоги
* QuickSortSerial и QuickSortParallel - реализации сортировок
* Тест QuickSortTest - cодержит два теста для замера времени выполнения а также валидности сортировки массива
***
##Вывод:
 
1. Total execution time - serial: 7699ms
2. Total execution time - parallel with 1 threads: 10527ms
3. Total execution time - parallel with 2 threads: 5510ms
4. Total execution time - parallel with 3 threads: 4568ms
5. Total execution time - parallel with 4 threads: 4075ms

| Кол-во процессов         | T(serial)/T(parallel) |
| :----------------------: |:---------------------:|
| 1                        |          0.73         |
| 2                        |          1.40         |
| 2                        |          1.69         |
| 4                        |          1.89         |
***