package demorecyclers.android.icecode.com.demorecyclers;

/*import android.support.v7.widget.RecyclerView;
*/



import android.content.ClipData;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.LinearLayoutManager;
import android.view.DragEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import timber.log.Timber;


public class MainActivity extends AppCompatActivity implements View.OnTouchListener, View.OnDragListener {

    private RecyclerView oneRecycler, twoRecycler;
    private LinearLayoutManager oneLayoutManager = null, twoLayoutManager = null;

    private AdapterSimple adapterSimple1 = null, adapterSimple2 = null;

    List<DemoData> data1 = new ArrayList<>();
    List<DemoData> data2 = new ArrayList<>();

    private static final int OP_TO_FREE = 0;
    private static final int OP_ASSIGN  = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Timber.plant(new Timber.DebugTree());
        oneLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        twoLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        data1.add(new DemoData( R.mipmap.icn_demo_1 ,DemoData.SELECCIONADO ));
        data1.add(new DemoData( R.mipmap.icn_demo_2 ,DemoData.SELECCIONADO ));
        data1.add(new DemoData( R.mipmap.icn_demo_3 ,DemoData.SELECCIONADO ));
        data1.add(new DemoData( R.mipmap.icn_demo_4 ,DemoData.SELECCIONADO ));



        oneRecycler = (RecyclerView) findViewById(R.id.one);
        oneRecycler.setLayoutManager(oneLayoutManager);
        oneRecycler.setOnDragListener(this);
        adapterSimple1 = new AdapterSimple(this, data1);
        oneRecycler.setAdapter(adapterSimple1);



        twoRecycler = (RecyclerView) findViewById(R.id.two);
        twoRecycler.setLayoutManager(twoLayoutManager);
        twoRecycler.setOnDragListener(this);
        adapterSimple2 = new AdapterSimple(this, data2);
        twoRecycler.setAdapter(adapterSimple2);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onDrag(View v, DragEvent event) {
        Drawable enterShape = getResources().getDrawable(R.drawable.drope_shape_droptarget);
        Drawable normalShape = getResources().getDrawable(R.drawable.drop_shape);


        int action = event.getAction();
        switch (event.getAction()) {
            case DragEvent.ACTION_DRAG_STARTED:
                // do nothing
                break;
            case DragEvent.ACTION_DRAG_ENTERED:
                v.setBackgroundDrawable(enterShape);
                break;
            case DragEvent.ACTION_DRAG_EXITED:
                v.setBackgroundDrawable(normalShape);
                break;
            case DragEvent.ACTION_DROP:
                // Dropped, reassign View to ViewGroup
                View view = (View) event.getLocalState();

                //Vamos a intentar recuperar el tag
                DemoData data = null;

                try {
                    data = (DemoData)view.getTag();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                Timber.e("Tag del view es: "+data);
                int operation = -1;
                List<DemoData> dataSelected = new ArrayList<>(data1);
                List<DemoData> dataUnselected = new ArrayList<>(data2);


                if (data != null){
                    //Ahora trataremos de asignar
                    Timber.v("Yeeiii tenemos data :) ");


                    switch (data.asigned){
                        case DemoData.SELECCIONADO:
                            Timber.v("-- Esta SELECCIONADO!! debe ir a disponible");
                            operation = OP_TO_FREE;
                            break;
                        case DemoData.DISPONIBLE:
                            Timber.v("-- Esta disponible!! debe ir a SELECCIONADO");
                            operation = OP_ASSIGN;
                            break;
                    }

                    Iterator<DemoData> iter = null;
                    if (operation == OP_TO_FREE ){
                        iter = dataSelected.iterator();
                        while (iter.hasNext()){
                            DemoData temp = iter.next();
                            if (temp.resource == data.resource){
                                Timber.v("Bingo!!! encontramos, a reasignar");
                                temp.asigned = DemoData.DISPONIBLE;
                                dataUnselected.add(temp);
                                iter.remove();
                            }
                        }
                    }else{
                        iter = dataUnselected.iterator();
                        while (iter.hasNext()){
                            DemoData temp = iter.next();
                            if (temp.resource == data.resource){
                                Timber.v("Bingo!!! encontramos, a reasignar");
                                temp.asigned = DemoData.SELECCIONADO;
                                dataSelected.add(temp);
                                iter.remove();
                            }
                        }
                    }

                    Timber.d("Se supone que ya intercambiamos datos, ahora debemosactalizar adapters");

                    data1 = new ArrayList<>(dataSelected );
                    data2 = new ArrayList<>(dataUnselected);

                    adapterSimple1 = new AdapterSimple(this, data1);
                    adapterSimple2 = new AdapterSimple(this, data2);

                    oneRecycler.setLayoutManager(oneLayoutManager);
                    oneRecycler.setOnDragListener(this);
                    oneRecycler.setAdapter(adapterSimple1);

                    twoRecycler.setLayoutManager(twoLayoutManager);
                    twoRecycler.setOnDragListener(this);
                    twoRecycler.setAdapter(adapterSimple2);

                }else {
                    Timber.e(":( no hay data");
                }

                //Aqui deberia quitarlo del adapter owner


                /*ViewGroup owner = (ViewGroup) view.getParent();
                owner.removeView(view);

                //Aqui deberia asignarlo al adapter destino
                LinearLayout container = (LinearLayout) v;
                container.addView(view);

                view.setVisibility(View.VISIBLE);*/
                break;
            case DragEvent.ACTION_DRAG_ENDED:
                v.setBackgroundDrawable(normalShape);
            default:
                break;
        }
        return true;
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
            ClipData data = ClipData.newPlainText("", "");
            View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(view);
            view.startDrag(data, shadowBuilder, view, 0);
            view.setVisibility(View.INVISIBLE);
            return true;
        } else {
            return false;
        }
    }

    class DemoData {
        public static final int SELECCIONADO = 0;
        public static final int DISPONIBLE   = 1;
        public Integer resource;
        public Integer asigned;

        public DemoData(Integer resource , Integer asigned){
            this.resource = resource;
            this.asigned = asigned;
        }
    }
}
