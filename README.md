# phonetics-game
Phonetics Game: Тест Фонетики  
JavaFX-based.

Abstract
-
A small test on the correlation of phonemes and their written representation.  
Available languages: German.


Небольшой тест на соотнесение фонем и их письменного представления.
Есть возможность настройки количества и длительности раундов. Ведется простая статистика результатов.  
Доступные языки: немецкий.

Описание игры
-
Игра заключается в том, что игрок выбирает букву или сочитание букв, которое соответствует звуку, воспроизведенному в начале хода. После завершения игры становится доступна статистика по даной игре. Так же игроку доступна общая статистика профиля.


Правила
-
* В начале каждого хода воспроизводится звук, соответствующий данному ходу.  
* Гласные звуки озвучиваются отдельно, согласные звуки озвучиваются вместе с гласным [a] (перед ним) и/или между двумя гласными [a].  
* Игроку дается ограниченное время на выбор ответа.  
* Новый ход начинается, когда истекает время на ход или когда игрок дал ответ (вне зависимости от корректности ответа).  
* Ответ считается верным, если буква или сочитание букв, выбранное игрокои, на письме в рамках данного языка может соответствовать звуку, воспроизведенному в данном ходу. В противном случае ответ считается ошибочным.  
* В случае, если время на ход закончилось, а ответ не был дан, ход засчитывается как ход с ошибочным ответом.  
* В течении хода игрок может повторно воспроизводить звук неограниченное количество раз.  
* Игрок может поставить игру на пазу, при этом у него не будет возможности выбрать ответ или прослушать звук повторно.  
* Игрок может прервать игру, тогда текущая игра будет закончена, а все оставшиеся ходы, начиная с текущего, будут защитаны как ошибочные.  


Горячие клавиши
-
<pre>Действие	Сочетание клавиш  
Новая игра          Ctrl+N  
Пауза               Ctrl+P  
Завершить игру      Ctrl+E  
Статистика профиля  Ctrl+S  
Настройки           Ctrl+C  
Выход               Ctrl+Q  
</pre>
