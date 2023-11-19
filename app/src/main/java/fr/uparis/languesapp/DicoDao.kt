package fr.uparis.languesapp

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface DicoDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun addWords(vararg  word : Word) : List<Long>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun addDicos(vararg dico : Dico) : List<Long>

    @Query("SELECT * FROM Dico")
    fun loadAllDico() : LiveData<List<Dico>>

    @Query("SELECT * from Dico where langue_dst like :lg || '%'")
    fun loadDicoByLanguageDst(lg : String) : LiveData<List<Dico>>

    @Query("SELECT * from Word")
    fun loadAllWord() : LiveData<List<Word>>

    @Query("SELECT * from Word where langue_src like :lg || '%'")
    fun loadWordByLanguageSrc(lg : String) : LiveData<List<Word>>

    @Query("SELECT * from Word where langue_dst like :lg || '%'")
    fun loadWordByLanguageDst(lg : String) : LiveData<List<Word>>

    @Query("SELECT * from Word where adr_url like :url || '%'")
    fun loadWordByAdr(url : String) : LiveData<List<Word>>

    @Query("SELECT * from Word where count >= 2")
    fun loadKnownWord() : LiveData<List<Word>>

    @Query("SELECT * from Word where count < 2 or :currentTime - timestamp > 1209600000")
    fun loadWordsToShow(currentTime : Long) : LiveData<List<Word>>

    @Query("SELECT * from Word where (langue_src = :src and langue_dst = :dst) and (count < 2 or :currentTime - timestamp > 1209600000)")
    fun loadWordsToShow(currentTime: Long, src : String, dst : String) : LiveData<List<Word>>

    @Query("UPDATE Word SET count=count+1 and timestamp=:timestamp where adr_url=:url")
    fun updateWordKnown(timestamp : Long, url : String) : Int

    @Delete
    fun deleteWords(vararg words : Word) : Int

    @Delete
    fun deleteWords(words : MutableList<Word>) : Int

    @Delete(entity = Word::class)
    fun deleteSomeWords(words : MutableList<Word>) : Int

    @Query("DELETE FROM Word where mot = :mot")
    fun deleteWordGiven(mot : String) : Int

    @Delete
    fun deleteDicos(vararg  dicos : Dico) : Int

    @Delete
    fun deleteDicos(dicos : MutableList<Dico>) : Int

    @Delete(entity = Dico::class)
    fun deleteSomeDicos(dicos : MutableList<Dico>) : Int

}