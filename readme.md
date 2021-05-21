# Курсовая 1 - Quicksort
***
## Классы:

* ParallelUtils - содержит реализации функций parallelFor, blockedParallelFor, map, scan, filter
* SerialUtils - их последовальные аналоги
* QuickSortSerial и QuickSortParallel - реализации сортировок
* Тест QuickSortTest - cодержит два теста для замера времени выполнения а также валидности сортировки массива
***
## Вывод:
 
1. Total execution time - serial: 7699ms
2. Total execution time - parallel with 1 threads: 10527ms
3. Total execution time - parallel with 2 threads: 5510ms
4. Total execution time - parallel with 3 threads: 4568ms
5. Total execution time - parallel with 4 threads: 4075ms

| Кол-во процессов         | T(serial)/T(parallel) |
| :----------------------: |:---------------------:|
| 1                        |          0.73         |
| 2                        |          1.40         |
| 3                        |          1.69         |
| 4                        |          1.89         |
***
# Курсовая 2 - BST
## Вывод:

0

| Кол-во процессов         | count |
| :----------------------: |:---------------------:|
| 1                        |      71170269     |
| 2                        |     142416773         |
| 3                        |         203462895         |
| 4                        |      281031849       |

10

| Кол-во процессов         |        count  |
| :----------------------: |:---------------------:|
| 1                        |       10949308       |
| 2                        |       33255019        |
| 3                        |      48103090      |
| 4                        |      65455287      |

50

| Кол-во процессов         | count                 |
| :----------------------: |:---------------------:|
| 1                        |     12660119       |
| 2                        |          28983761        |
| 3                        |     40608892      |
| 4                        |      51365805      |
