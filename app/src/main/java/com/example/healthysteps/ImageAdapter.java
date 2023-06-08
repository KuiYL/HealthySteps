package com.example.healthysteps;

import android.content.Context;
import android.graphics.Typeface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import java.util.ArrayList;
import java.util.Arrays;

public class ImageAdapter extends BaseAdapter {
    private Context context;
    private final Integer[] imageIds;
    private final String[] stringArray;
    private ArrayList<String> filteredStringArray;

    public ImageAdapter(Context context) {
        this.context = context;
        this.imageIds = new Integer[]{R.drawable.image1, R.drawable.image2, R.drawable.image3,
                R.drawable.image4, R.drawable.image5, R.drawable.image6, R.drawable.image7,
                R.drawable.image8, R.drawable.image9, R.drawable.image10};
        this.stringArray = new String[]{"Омлет с овощами", "Салат с курицей", "Тунец с киноа",
                "Овощное рагу", "Греческий салат", "Куриные котлеты", "Тыквенный суп",
                "Киш с овощами", "Салат из авокадо", "Рыба на гриле"};
        this.filteredStringArray = new ArrayList<>(Arrays.asList(stringArray));
    }

    @Override
    public int getCount() {
        return filteredStringArray.size();
    }

    @Override
    public Object getItem(int position) {
        return filteredStringArray.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LinearLayout layout;

        if (convertView == null) {
            layout = new LinearLayout(context);
            layout.setLayoutParams(new GridView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            layout.setOrientation(LinearLayout.VERTICAL);
            layout.setPadding(8, 8, 8, 8);

            ImageView imageView = new ImageView(context);
            imageView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 350));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            layout.addView(imageView);

            TextView textView = new TextView(context);
            textView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            textView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            textView.setTextSize(15);
            textView.setTypeface(null, Typeface.BOLD_ITALIC);
            textView.setPadding(8, 8, 8, 8);
            layout.addView(textView);

            layout.setTag(new ViewHolder(imageView, textView));
        } else {
            layout = (LinearLayout) convertView;
        }

        ViewHolder viewHolder = (ViewHolder) layout.getTag();
        int originalPosition = Arrays.asList(stringArray).indexOf(filteredStringArray.get(position));
        viewHolder.imageView.setImageResource(imageIds[originalPosition]);
        viewHolder.textView.setText(filteredStringArray.get(position));

        layout.setOnClickListener(v -> {
            // Создание нового фрагмента
            Fragment newFragment = new RecipeFragment();

            // Получите ссылку на менеджер фрагментов из активности
            RecipeFragment.pos = Arrays.asList(stringArray).indexOf(filteredStringArray.get(position));

            replaceFragment(newFragment);
        });

        return layout;
    }

    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = ((AppCompatActivity) context).getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout, fragment);
        fragmentTransaction.commit();
    }

    private static class ViewHolder {
        ImageView imageView;
        TextView textView;

        ViewHolder(ImageView imageView, TextView textView) {
            this.imageView = imageView;
            this.textView = textView;
        }
    }

    public String[] getStringArray() {
        return stringArray;
    }

    public void setFilteredStringArray(ArrayList<String> filteredItems) {
        filteredStringArray = filteredItems;
        notifyDataSetChanged();
    }
}