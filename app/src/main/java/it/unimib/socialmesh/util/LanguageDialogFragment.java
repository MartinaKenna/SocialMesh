package it.unimib.socialmesh.util;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import it.unimib.socialmesh.ui.main.ProfileFragment;
import it.unimib.socialmesh.util.LocaleManager;
import java.util.Locale;

public class LanguageDialogFragment extends DialogFragment {

    private Context context;

    @NonNull
    @Override
    public AlertDialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        context = requireContext();

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Select Language");

        final ArrayAdapter<String> adapter = new ArrayAdapter<>(context, android.R.layout.simple_list_item_1);
        adapter.add("Italiano");
        adapter.add("English");
        adapter.add("Español");
        adapter.add("中文");

        builder.setAdapter(adapter, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String selectedLanguage = adapter.getItem(which);
                setLocale(selectedLanguage);
            }
        });

        return builder.create();
    }

    private void setLocale(String selectedLanguage) {
        LocaleManager lang = new LocaleManager(getContext());
        switch (selectedLanguage) {
            case "Italiano":
                lang.updateResource("it");
                break;
            case "English":
                lang.updateResource("en");
                break;
            case "Español":
                lang.updateResource("es");
                break;
            case "中文":
                lang.updateResource("zh");
                break;
            default:
             break;
        }

        Log.d("Language", lang.toString());
        dismiss();
        getActivity().recreate();
    }

}
