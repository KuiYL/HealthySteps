package com.example.healthysteps;

import android.graphics.Typeface;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class RecipeFragment extends Fragment {
    public static int pos;
    private String h1;
    private String textR;

    private RecyclerView recyclerView;
//    private IngredientAdapter adapter;
    private final Integer[] imageIds = {R.drawable.image1, R.drawable.image2, R.drawable.image3,
            R.drawable.image4,R.drawable.image5,R.drawable.image6, R.drawable.image7,
            R.drawable.image8,R.drawable.image9,R.drawable.image10};
    private final String[] h = {"Омлет с овощами","Салат с курицей","Тунец с киноа",
            "Овощное рагу","Греческий салат", "Куриные котлеты","Тыквенный суп",
            "Киш с овощами","Салат из авокадо","Рыба на гриле"};
    private final String[] textRecipe = {"1. Взбейте яйца с молоком.\n" +
            "2. Добавьте нарезанные овощи (помидоры, шпинат, перец и т.д.).\n" +
            "3. Приправьте специями по вкусу.\n" +
            "4. Жарьте на сковороде до готовности.",
            "1. Смешайте нарезанную куриную грудку с свежими овощами (листья салата, огурцы, авокадо и др.).\n" +
            "2. Добавьте зелень (укроп, петрушку).\n" +
            "3.Полейте лимонным соусом или оливковым маслом.",
            "1. Приготовьте киноа по инструкции на упаковке.\n" +
            "2. Обжарьте кусочки тунца на гриле или сковороде.\n" +
            "3. Подайте тунец на киноа, добавьте лимонный сок и зелень.",
    "1. Нарежьте разнообразные овощи (морковь, брокколи, цветную капусту, лук и др.).\n" +
            "2. Тушите овощи на сковороде с минимальным количеством масла.\n" +
            "3. Приправьте специями и подайте с гарниром (квиноа, картофель).",
    "1. Смешайте нарезанные огурцы, помидоры, перец, красный лук, маслины и сыр фета.\n" +
            "2. Полейте оливковым маслом и добавьте сок лимона.\n" +
            "3. Посыпьте сухими травами (орегано, базилик).",
    "1. Измельчите куриную грудку в блендере.\n" +
            "2. Добавьте яйцо, сухие травы, соль, перец и мелко нарезанный лук.\n" +
            "3. Сформируйте котлеты и пожарьте на сковороде.",
            "1. Пассеруйте нарезанную тыкву, морковь, лук и чеснок на сковороде.\n" +
                    "2. Добавьте овощной бульон и варите до мягкости.\n" +
                    "3. Смешайте суп в блендере до получения однородной консистенции.",
            "1. Приготовьте тесто из муки, яйца и масла.\n" +
                    "2. Нарежьте овощи (помидоры, перец, цукини) и обжарьте на сковороде.\n" +
                    "3. Выложите овощи на тесто и запекайте в духовке до готовности.",
            "1. Смешайте нарезанное авокадо, вареные креветки, помидоры черри и зелень.\n" +
                    "2. Полейте лимонным соусом или оливковым маслом.",
            "1. Приготовьте филе рыбы (лосось, треска) на гриле или в духовке.\n" +
                    "2. Подайте с печеными овощами (брокколи, морковь, кабачок) и зеленью."
    };

    private final String[] ingredients = {"Яйцо куриное\t6шт._Помидоры\t3шт._Лук репчатый\t1шт._Базилик свежий\t4ветки_Сметана\t2ст.л_Масло сливочное\t1ст.л" ,
            "Салат Айсберг\t400 гр._Куриное филе\t280 гр._Помидоры черри\t10 шт._Батон\t220 гр._Пармезан\t80 гр._Чеснок\t1 зубч._Растительное масло\t1 стол.л.",
            "Салат Айсберг\t400 гр._Киноа вареная\t3-4 ст.л._Тунец консервированный\t130 гр._Помидоры черри\t5-6 шт._Масло оливковое\t2 ст.л.",
            "Баклажаны\t300 гр._Картошка\t350 гр._Помидоры\t1 шт._Болгарский перец\t1 шт._Лук репчатый\t1 шт._Чеснок\t2 зубч._Лавровый лист\t1 шт._Соль\t1 чайн.л._Перец горошком\t2 шт._Перец черный молотый\t0.25 чайн.л._Растительное масло\t4 стол.л._Хмели-сунели\tпо вкусу",
            "Куриное филе\t200 гр._Помидоры черри\t150 гр._Брынза\t80 гр._Маслины\t50 гр._Болгарский перец\t1 шт._Огурцы\t1 шт._Лук (красный)\t1 шт._Листья салата\t30 гр._Перец черный молотый\tпо вкусу_Соль\tпо вкусу",
            "Куриное филе\t166.7 гр._Твёрдый сыр\t33.3 гр._Крахмал\t1 стол.л._Кефир (можно взять 50 мл кефира + 50 гр майонеза)\t33 мл_Зелень\t3.3 гр._Специи сухие\tпо вкусу_Соль\tпо вкусу",
            "Тыква (очищенная)\t350 гр._Картошка (небольшая)\t1 шт._Морковь\t1 шт._Лук репчатый\t1 шт._Болгарский перец\t1 шт._Соль\tпо вкусу_Куриный бульон\t350 мл_Сливки (10%)\t100 мл_Твёрдый сыр\t50 гр._Растительное масло\t2 стол.л._Мускатный орех (щепотка)\tпо вкусу_Специи сухие\tпо вкусу_Зелень (для подачи, по желанию)\tпо вкусу",
            "Пшеничная мука\t2 стак._Сливочное масло\t200 гр._Яйца\t1 шт._Соль\tпо вкусу_Соль\tпо вкусу_Куриное филе\t300 гр._Грибы (шампиньоны)\t400 гр._Яйца\t2 шт._Твёрдый сыр\t100 гр._Сметана\t100 гр._Растительное масло\t20 мл.",
            "Авокадо (крупный)\t0.5 шт._Помидоры черри\t50 гр._Лук (красный)\t5 гр._Чеснок\t0.5 зубч._Лимоны\t0.3 шт._Оливковое масло\t1 чайн.л._Соль\tпо вкусу_Зелень\tпо вкусу_Перец острый молотый\tпо вкусу",
            "Горбуша\t600 гр._Лимоны\t0.3 шт._Оливковое масло\t30 гр._Соль\tпо вкусу_Специи сухие\tпо вкусу"
    };
    public RecipeFragment(){

    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recipe, container, false);
        ImageView imageViewRecipe = view.findViewById(R.id.imageviewRecep);
        imageViewRecipe.setImageResource(imageIds[pos]);
        TextView H1 = view.findViewById(R.id.textViewRecep);

        getIngredientList(view, pos);
        h1 = h[pos];
        H1.setText(h1);
        TextView text = view.findViewById(R.id.textViewRecipe);
        textR = textRecipe[pos];
        text.setText(textR);
        ImageView backButton = view.findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                replaceFragment(new ReceiptFragment());
            }
        });
        return  view;
    }
    private void replaceFragment(Fragment fragment){
        FragmentManager fragmentManager = (requireActivity()).getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout,fragment);
        fragmentTransaction.commit();
    }

    private void getIngredientList(View view,int position) {
        LinearLayout ingredientsLayout = view.findViewById(R.id.checkbox);
        List<Ingredient> ingredientList = new ArrayList<>();
        String[] list = ingredients[position].split("_");
        for (String s : list) {
            ingredientList.add(new Ingredient(s));
        }

        for (Ingredient ingredient : ingredientList) {
            CheckBox checkBox = new CheckBox(requireContext());
            checkBox.setText(ingredient.getName());

            // Установите нужный шрифт здесь
            float textSizeSP = 16; // Размер шрифта в SP
            checkBox.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSizeSP);
            Typeface typeface = ResourcesCompat.getFont(requireContext(), R.font.roboto_regular);
            checkBox.setTypeface(typeface);

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            params.gravity = Gravity.TOP; // Установите гравитацию вверх

            ingredientsLayout.addView(checkBox, params);
        }
    }

}