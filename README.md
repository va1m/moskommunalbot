# MosKommunalBot

A telegram bot calculating communal expenses for hot, cold
 water, for water disposing, and for electricity in Moscow.

## Scenario

* User selects "new calculation"
* bot: enter cold water last meter readings
* user: enters
* bot: enter hot water current meter readings
* user: enters
* bit: repeats the same questions for hot water and electricity
* bot: parses the pages from links below, finds prices,
for current date and calculates payment amounts.
* bot: send a message to user in following format:
```
Cold water:
- the price sets from 01.07.2020
- amount to pay: 3456 RUB (123L × 123 RUB/L)
Hot water:
- the price sets from 01.08.2020
- amount to pay: 3456 RUB (123L × 123 RUB/L)
Water disposal:
- the price sets from 01.09.2020
- amount to pay: 3456 RUB (345L × 123 RUB/L)
Electricity:
- the price sets from 01.10.2020
- amount to pay: 3456 RUB (123KW × 123 RUB/KW)
```

## The prices are from

* Cold water and disposing: http://www.mosvodokanal.ru/forabonents/tariffs/
* Hot water: https://online.moek.ru/clients/tarify-i-raschety/tarify
* Electricity: https://www.mosenergosbyt.ru/individuals/tariffs-n-payments/tariffs-msk/kvartiry-i-doma-s-elektricheskimi-plitami.php
