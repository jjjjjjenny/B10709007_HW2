package com.example.b10709007_hw2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.b10709007_hw2.data.WaitlistContract;
import com.example.b10709007_hw2.data.WaitlistDbHelper;

public class MainActivity extends AppCompatActivity {
    private GuestListAdapter mAdapter;
    public static SQLiteDatabase mDb;

    private RecyclerView waitlistRecyclerView;

    private final static String LOG_TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        waitlistRecyclerView = (RecyclerView) this.findViewById(R.id.all_guests_list_view);
        waitlistRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        WaitlistDbHelper dbHelper = new WaitlistDbHelper(this);

        mDb = dbHelper.getWritableDatabase();

        Cursor cursor = getAllGuests();//取得所有的資料

        mAdapter = new GuestListAdapter(this,cursor);//傳過去給adapter

        waitlistRecyclerView.setAdapter(mAdapter);


        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {//設定左滑右滑

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {//不設定
                return false;
            }

            @Override
            public void onSwiped(final RecyclerView.ViewHolder viewHolder, int swipeDir) {

                AlertDialog.Builder a=new AlertDialog.Builder(MainActivity.this);

                a.setTitle("確定？");
                a.setMessage("是否確定刪除呢！");
                a.setPositiveButton("Ok",new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        // TODO Auto-generated method stub
                        Toast.makeText(MainActivity.this, "確定刪除",Toast.LENGTH_SHORT).show();

                        long id = (long) viewHolder.itemView.getTag();//取得劃掉的是哪一個

                        removeGuest(id);//刪除

                        mAdapter.swapCursor(getAllGuests());//更新
                    }

                });

                a.setNegativeButton("Cancel",new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        // TODO Auto-generated method stub
                        Toast.makeText(MainActivity.this, "取消",Toast.LENGTH_SHORT).show();

                        mAdapter.swapCursor(getAllGuests());//更新
                        arg0.dismiss();
                    }

                });

                a.show();//記得R
            }

        }).attachToRecyclerView(waitlistRecyclerView);//目標是...



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {//menu的畫面
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.visualizer_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {//當點選哪個前往哪ㄋ
        int id=item.getItemId();
        if(id==R.id.action_settings){
            Intent Settings=new Intent(this,SettingsActivity.class);
            startActivity(Settings);
            return true;
        }
        else if(id==R.id.add){
            Intent Adds=new Intent(this,AddGuestActivity.class);
            startActivity(Adds);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private Cursor getAllGuests() {//取得所有資料的Sql
        return mDb.query(
                WaitlistContract.WaitlistEntry.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                WaitlistContract.WaitlistEntry.COLUMN_TIMESTAMP
        );
    }

    private boolean removeGuest(long id) {//刪除的Sql
        return mDb.delete(WaitlistContract.WaitlistEntry.TABLE_NAME, WaitlistContract.WaitlistEntry._ID + "=" + id, null) > 0;
    }

}
