package com.dorukaneskiceri.kotlinartbook

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.ArrayAdapter
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val artNameList = ArrayList<String>()
        val artIdList = ArrayList<Int>()

        val adapter = ArrayAdapter(this,android.R.layout.simple_list_item_1,artNameList)
        listView.adapter = adapter

        try{
            val database = openOrCreateDatabase("Art", Context.MODE_PRIVATE,null)
            val cursor = database.rawQuery("SELECT * FROM arts",null)
            val nameIndex = cursor.getColumnIndex("artName")
            val IdIndex = cursor.getColumnIndex("id")

            while(cursor.moveToNext()){
                artNameList.add(cursor.getString(nameIndex))
                artIdList.add(cursor.getInt(IdIndex))
            }
            adapter.notifyDataSetChanged()
            cursor.close()

        }catch(e: Exception){
            e.printStackTrace()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {

        //Menu-Inflater
        val menuInflater = menuInflater
        menuInflater.inflate(R.menu.add_art,menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == R.id.add_art_item){
            val intent = Intent(this,ArtActivity::class.java)
            startActivity(intent)
        }
        return super.onOptionsItemSelected(item)
    }
}
