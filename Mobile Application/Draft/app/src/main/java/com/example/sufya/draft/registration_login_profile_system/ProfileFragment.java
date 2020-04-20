package com.example.sufya.draft.registration_login_profile_system;

/**
 * @Author Ajmal, R. (2016).
 * @Website https://www.learn2crack.com/2016/04/android-login-registration-php-mysql-client.html
 *
 * New functions created save expenses, load expenses and function
 * Sufyan Ahmed 2017
 */

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.AppCompatButton;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sufya.draft.Constants;
import com.example.sufya.draft.Database.ExpenseDbAdapter;
import com.example.sufya.draft.R;

import com.example.sufya.draft.models.Expense;
import com.example.sufya.draft.service.AppConfig;
import com.example.sufya.draft.service.RequestInterface;
import com.example.sufya.draft.models.ServerRequest;
import com.example.sufya.draft.models.ServerResponse;
import com.example.sufya.draft.models.User;
import com.example.sufya.draft.service.RestClient;
import com.example.sufya.draft.service.Service;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ProfileFragment extends Fragment implements View.OnClickListener {

    private TextView tv_name,tv_email,tv_message;
    private SharedPreferences pref;
    private AppCompatButton btn_change_password,btn_logout, btn_load_expenses, btn_save_expenses;
    private EditText et_old_password,et_new_password;
    private AlertDialog dialog;
    private ProgressBar progress;
    private ExpenseDbAdapter dbHelper;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile,container,false);
        initViews(view);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {

        pref = getActivity().getSharedPreferences("myprefs", 0);

        tv_name.setText("Welcome : "+pref.getString(Constants.NAME,""));
        tv_email.setText(pref.getString(Constants.EMAIL,""));

    }

    /**
     *
     * @param view
     */
    private void initViews(View view){

        tv_name = (TextView)view.findViewById(R.id.tv_name);
        tv_email = (TextView)view.findViewById(R.id.tv_email);
        btn_change_password = (AppCompatButton)view.findViewById(R.id.btn_chg_password);
        btn_load_expenses = (AppCompatButton)view.findViewById(R.id.btn_load_expenses);
        btn_save_expenses = (AppCompatButton)view.findViewById(R.id.btn_save_expenses);
        btn_logout = (AppCompatButton)view.findViewById(R.id.btn_logout);
        btn_change_password.setOnClickListener(this);
        btn_load_expenses.setOnClickListener(this);
        btn_save_expenses.setOnClickListener(this);
        btn_logout.setOnClickListener(this);

    }

    /**
     *
     */
    private void showDialog(){

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_change_password, null);
        et_old_password = (EditText)view.findViewById(R.id.et_old_password);
        et_new_password = (EditText)view.findViewById(R.id.et_new_password);
        tv_message = (TextView)view.findViewById(R.id.tv_message);
        progress = (ProgressBar)view.findViewById(R.id.progress);
        builder.setView(view);
        builder.setTitle("Change Password");
        builder.setPositiveButton("Change Password", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        dialog = builder.create();
        dialog.show();
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String old_password = et_old_password.getText().toString();
                String new_password = et_new_password.getText().toString();
                if(!old_password.isEmpty() && !new_password.isEmpty()){

                    progress.setVisibility(View.VISIBLE);
                    changePasswordProcess(pref.getString(Constants.EMAIL,""),old_password,new_password);

                }else {

                    tv_message.setVisibility(View.VISIBLE);
                    tv_message.setText("Fields are empty");
                }
            }
        });
    }

    /**
     *
     * @param v
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()){

            case R.id.btn_chg_password:
                showDialog();
                break;
            case R.id.btn_load_expenses:
                loadExpenses();
                break;
            case R.id.btn_save_expenses:
                saveExpenses();
                break;
            case R.id.btn_logout:
                logout();
                break;
        }
    }

    /**
     *
     */
    private void logout() {
        SharedPreferences.Editor editor = pref.edit();
        editor.putBoolean(Constants.IS_LOGGED_IN,false);
        editor.putString(Constants.EMAIL,"");
        editor.putString(Constants.NAME,"");
        editor.putString(Constants.UNIQUE_ID,"");
        editor.putString(Constants.FIRST_TIME,"first_time");
        editor.apply();
        dbHelper = new ExpenseDbAdapter(getActivity());
        dbHelper.open();
        dbHelper.deleteAllExpenses();
        dbHelper.close();
        goToLogin();

    }

    /**
     *
     */
    private void goToLogin(){

        Fragment login = new LoginFragment();
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace(R.id.fragment_frame,login);
        ft.commit();
    }

    /**
     *
     * @param email
     * @param old_password
     * @param new_password
     */
    private void changePasswordProcess(String email,String old_password,String new_password){

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        RequestInterface requestInterface = retrofit.create(RequestInterface.class);

        User user = new User();
        user.setEmail(email);
        user.setOld_password(old_password);
        user.setNew_password(new_password);
        ServerRequest request = new ServerRequest();
        request.setOperation(Constants.CHANGE_PASSWORD_OPERATION);
        request.setUser(user);
        Call<ServerResponse> response = requestInterface.operation(request);

        response.enqueue(new Callback<ServerResponse>() {
            @Override
            public void onResponse(Call<ServerResponse> call, retrofit2.Response<ServerResponse> response) {

                ServerResponse resp = response.body();
                if(resp.getResult().equals(Constants.SUCCESS)){
                    progress.setVisibility(View.GONE);
                    tv_message.setVisibility(View.GONE);
                    dialog.dismiss();
                    Snackbar.make(getView(), resp.getMessage(), Snackbar.LENGTH_LONG).show();

                }else {
                    progress.setVisibility(View.GONE);
                    tv_message.setVisibility(View.VISIBLE);
                    tv_message.setText(resp.getMessage());
                }
            }

            @Override
            public void onFailure(Call<ServerResponse> call, Throwable t) {

                Log.d(Constants.TAG,"failed");
                progress.setVisibility(View.GONE);
                tv_message.setVisibility(View.VISIBLE);
                tv_message.setText(t.getLocalizedMessage());

            }
        });
    }

    /**
     *
     */
    private void loadExpenses() {
        dbHelper = new ExpenseDbAdapter(getActivity());

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity(), R.style.MyDialogTheme);
        alertDialog.setTitle("Load Expenses...");
        alertDialog.setMessage("This Will Delete All Expenses And Load Expenses From Server");
        alertDialog.setIcon(R.drawable.reg_logo);
        alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog,int which) {

                dbHelper.open(); //Open Database
                dbHelper.deleteAllExpenses();
                dbHelper.close();

                final String uniqueid = pref.getString(Constants.UNIQUE_ID,"");

                Service serviceAPI = RestClient.getClient();

                Call<List<Expense>> loadSizeCall = serviceAPI.loadSizes(uniqueid);
                loadSizeCall.enqueue(new Callback<List<Expense>>() {
                    @Override
                    public void onResponse(Call<List<Expense>> call, Response<List<Expense>> response) {
                        for(Expense expense: response.body()) {

                            dbHelper.open();
                            dbHelper.createExpense(expense.getUniqueid(), expense.getCategory(), expense.getSub_category(), expense.getDescription(), expense.getPrice(), expense.getTimestamp());
                            Toast.makeText(getActivity(), "Loading Expenses", Toast.LENGTH_SHORT).show();

                        }
                    }

                    @Override
                    public void onFailure(Call<List<Expense>> call, Throwable t) {

                    }
                });

            }
        });

        alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                // Write your code here to invoke NO event
                Toast.makeText(getActivity(), "You clicked NO", Toast.LENGTH_SHORT).show();
                dialog.cancel();
            }
        });

        alertDialog.show();

    }

    /**
     *
     */
    private void saveExpenses() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity(), R.style.MyDialogTheme);
        alertDialog.setTitle("Save Expenses...");
        alertDialog.setMessage("This Will Delete All Expenses From Server And Save New Ones");
        alertDialog.setIcon(R.drawable.reg_logo);
        alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog,int which) {

                deleteServerExpenses(pref.getString(Constants.UNIQUE_ID,""));
            }
        });

        alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                // Write your code here to invoke NO event
                Toast.makeText(getActivity(), "You clicked NO", Toast.LENGTH_SHORT).show();
                dialog.cancel();
            }
        });
        alertDialog.show();

    }

    private void deleteServerExpenses(String uniqueid) {
        RestAdapter adapter = new RestAdapter.Builder()
                .setEndpoint("http://www.moblet.club/moblet") //Setting the Root URL
                .build();

        AppConfig.deleteRequest api = adapter.create(AppConfig.deleteRequest.class);

        api.insertData(
                uniqueid,

                new retrofit.Callback<retrofit.client.Response>() {
                    @Override
                    public void success(retrofit.client.Response result, retrofit.client.Response response) {
                        try {

                            BufferedReader reader = new BufferedReader(new InputStreamReader(result.getBody().in()));
                            String resp;
                            resp = reader.readLine();
                            Log.d("success", "" + resp);

                            JSONObject jObj = new JSONObject(resp);
                            int success = jObj.getInt("success");

                            if (success == 1) {
                                Toast.makeText(getActivity(), "Successfully All Deleted", Toast.LENGTH_SHORT).show();
                                function();

                            } else {
                                Toast.makeText(getActivity(), "Deletion Failed, try again", Toast.LENGTH_SHORT).show();
                            }

                        } catch (IOException e) {
                            Log.d("Exception", e.toString());
                        } catch (JSONException e) {;
                            Log.d("JsonException", e.toString());
                        }
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        Toast.makeText(getActivity(), error.toString(), Toast.LENGTH_LONG).show();
                    }
                }
        );

    }

    /**
     *
     */
    public void function () {
        dbHelper = new ExpenseDbAdapter(getActivity());
        dbHelper.open(); //Open Database#

        Cursor cursor2 = dbHelper.fetchAllExpenses();

        try {
            while (cursor2.moveToNext()) {
                final String uniqueid = cursor2.getString(cursor2.getColumnIndexOrThrow("uniqueid"));
                final String description = cursor2.getString(cursor2.getColumnIndexOrThrow("description"));
                final String price = cursor2.getString(cursor2.getColumnIndexOrThrow("price"));
                final String category = cursor2.getString(cursor2.getColumnIndexOrThrow("category"));
                final String subcategory = cursor2.getString(cursor2.getColumnIndexOrThrow("subcategory"));
                final String timestamp = cursor2.getString(cursor2.getColumnIndexOrThrow("timestamp"));

                RestAdapter adapter = new RestAdapter.Builder()
                        .setEndpoint("http://www.moblet.club/moblet") //Setting the Root URL
                        .build();

                AppConfig.insert api = adapter.create(AppConfig.insert.class);

                api.insertData(
                        uniqueid,
                        category,
                        subcategory,
                        description,
                        price,
                        timestamp,

                        new retrofit.Callback<retrofit.client.Response>() {
                            @Override
                            public void success(retrofit.client.Response result, retrofit.client.Response response) {
                                try {

                                    BufferedReader reader = new BufferedReader(new InputStreamReader(result.getBody().in()));
                                    String resp;
                                    resp = reader.readLine();
                                    Log.d("success", "" + resp);

                                    JSONObject jObj = new JSONObject(resp);
                                    int success = jObj.getInt("success");

                                    if (success == 1) {

                                        Toast.makeText(getActivity(), "Successfully inserted", Toast.LENGTH_SHORT).show();

                                    } else {
                                        Toast.makeText(getActivity(), "Insertion Failed, try again", Toast.LENGTH_SHORT).show();
                                    }

                                } catch (IOException e) {
                                    Log.d("Exception", e.toString());
                                } catch (JSONException e) {;
                                    Log.d("JsonException", e.toString());
                                }

                            }

                            @Override
                            public void failure(RetrofitError error) {
                                Toast.makeText(getActivity(), error.toString(), Toast.LENGTH_LONG).show();
                            }
                        }
                );
            }
        } finally {
            cursor2.close();
            dbHelper.close();
        }

    }

}
