package muffsandchocss.com;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import muffsandchocss.com.Interface.ItemClickListener;
import muffsandchocss.com.ViewHolder.FoodViewHolder;
import muffsandchocss.com.mandc.R;
import muffsandchocss.com.model.Food;

public class FoodList extends AppCompatActivity {

    RecyclerView recycler_food;
    RecyclerView.LayoutManager layoutManager;

    FirebaseDatabase database;
    DatabaseReference foodList;

    String categoryId ="";

    FirebaseRecyclerAdapter<Food,FoodViewHolder> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_list);

        //Init Firebase
        database = FirebaseDatabase.getInstance();
        foodList = database.getReference("Foods");

        recycler_food = (RecyclerView) findViewById(R.id.recycler_food);
        recycler_food.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recycler_food.setLayoutManager(layoutManager);


        //Get the gategory Id
        if (getIntent() !=null){
            categoryId = getIntent().getStringExtra("CategoryId");

            if (!categoryId.isEmpty() && categoryId !=null){
                loadListFood(categoryId);
                Toast.makeText(FoodList.this,"Category Id " + categoryId,Toast.LENGTH_SHORT).show();
            }
        }

    }

    private void loadListFood(String categoryId) {

        adapter = new FirebaseRecyclerAdapter<Food, FoodViewHolder>(Food.class,
                R.layout.food_item,
                FoodViewHolder.class,
                foodList.orderByChild("MenuId").equalTo(categoryId)   //SELECT * FROM Food where menuId =
                ) {
            @Override
            protected void populateViewHolder(FoodViewHolder viewHolder, Food model, int position) {

                viewHolder.txtFoodName.setText(model.getName());
                Picasso.with(getBaseContext()).load(model.getImage())
                        .into(viewHolder.imageViewFoodImage);

                final Food local = model;
                viewHolder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {
                        //Toast.makeText(FoodList.this,""+ local.getName(),Toast.LENGTH_SHORT).show();
                    }
                });
                //Toast.makeText(FoodList.this,"Menu Name" + model.getName(),Toast.LENGTH_SHORT).show();
            }
        };

        //set adapter
        recycler_food.setAdapter(adapter);

    }
}
