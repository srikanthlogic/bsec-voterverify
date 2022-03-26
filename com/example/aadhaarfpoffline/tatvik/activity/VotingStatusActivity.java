package com.example.aadhaarfpoffline.tatvik.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.exifinterface.media.ExifInterface;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.vectordrawable.graphics.drawable.PathInterpolatorCompat;
import com.example.aadhaarfpoffline.tatvik.BuildConfig;
import com.example.aadhaarfpoffline.tatvik.GetDataService;
import com.example.aadhaarfpoffline.tatvik.LocaleHelper;
import com.example.aadhaarfpoffline.tatvik.R;
import com.example.aadhaarfpoffline.tatvik.UserAuth;
import com.example.aadhaarfpoffline.tatvik.adapter.VotingHistoryAdapter;
import com.example.aadhaarfpoffline.tatvik.config.RetrofitClientInstance;
import com.example.aadhaarfpoffline.tatvik.database.DBHelper;
import com.example.aadhaarfpoffline.tatvik.model.VoterDataNewModel;
import com.example.aadhaarfpoffline.tatvik.model.VotingHistoryModel;
import com.example.aadhaarfpoffline.tatvik.network.VoterByUserIdGetResponse;
import com.example.aadhaarfpoffline.tatvik.util.Const;
import com.facebook.drawee.view.SimpleDraweeView;
import java.io.File;
import java.util.HashMap;
import java.util.List;
import org.apache.commons.io.IOUtils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
/* loaded from: classes2.dex */
public class VotingStatusActivity extends AppCompatActivity implements VotingHistoryAdapter.OnItemClickListener {
    private TextView aadhaaNonAaadhaatCount;
    private VotingHistoryAdapter adapter;
    private TextView blockBooth;
    Context context;
    DBHelper db;
    String lan;
    private List<VotingHistoryModel> list;
    private RecyclerView recyclerView;
    Resources resources;
    private TextView stateDistrict;
    private TextView txtNumAllVoted;
    private TextView txtNumFemaleVoted;
    private TextView txtNumMaleVoted;
    private TextView txtNumRejected;
    private UserAuth userAuth;
    private List<VotingHistoryModel> votingHistoryModelList;

    @Override // androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_votingstatus);
        this.lan = LocaleHelper.getLanguage(this);
        this.context = LocaleHelper.setLocale(this, this.lan);
        this.resources = this.context.getResources();
        this.db = new DBHelper(this);
        this.userAuth = new UserAuth(this);
        this.txtNumAllVoted = (TextView) findViewById(R.id.num_all_voted);
        this.txtNumFemaleVoted = (TextView) findViewById(R.id.num_female_voted);
        this.txtNumMaleVoted = (TextView) findViewById(R.id.num_male_voted);
        this.txtNumRejected = (TextView) findViewById(R.id.num_rejected);
        this.stateDistrict = (TextView) findViewById(R.id.state_district);
        this.blockBooth = (TextView) findViewById(R.id.block_booth);
        this.aadhaaNonAaadhaatCount = (TextView) findViewById(R.id.aadhaarnonaadhaar);
        this.recyclerView = (RecyclerView) findViewById(R.id.recyclerview_voter_history_list);
        TextView textView = this.txtNumAllVoted;
        textView.setText("Voter Turnout:" + this.db.getNumbersVoted());
        TextView textView2 = this.txtNumFemaleVoted;
        textView2.setText("Female voters:" + this.db.getNumberFemalesVoted() + " Male Voters:" + this.db.getNumberMalesVoted());
        TextView textView3 = this.txtNumRejected;
        StringBuilder sb = new StringBuilder();
        sb.append("Rejected voters:");
        sb.append(this.db.getNumbersRejected());
        textView3.setText(sb.toString());
        if (this.lan.equalsIgnoreCase("en")) {
            TextView textView4 = this.stateDistrict;
            textView4.setText(this.resources.getString(R.string.panchayat_name) + ":" + this.userAuth.getPanchayat_NAME_EN() + " " + this.resources.getString(R.string.district_name_text) + ":" + this.userAuth.getDIST_NAME_EN() + " " + this.resources.getString(R.string.block_name) + ":" + this.userAuth.getBlock_NAME_EN());
        } else {
            TextView textView5 = this.stateDistrict;
            textView5.setText(this.resources.getString(R.string.panchayat_name) + ":" + this.userAuth.getPanchayat_NAME_HN() + " " + this.resources.getString(R.string.district_name_text) + ":" + this.userAuth.getDIST_NAME_HN() + " " + this.resources.getString(R.string.block_name) + ":" + this.userAuth.getBlock_NAME_HN());
        }
        TextView textView6 = this.blockBooth;
        textView6.setText(this.resources.getString(R.string.panchayat_id) + ":" + this.userAuth.getPanchayatId() + ", " + this.resources.getString(R.string.booth_no_text) + ":" + getBoothInFormat(this.userAuth.getBoothNo()) + "," + this.resources.getString(R.string.ward_no_text) + ":" + this.userAuth.getWardNo());
        TextView textView7 = this.aadhaaNonAaadhaatCount;
        StringBuilder sb2 = new StringBuilder();
        sb2.append("Voters by Aadhaar:");
        sb2.append(this.db.getAadhaarVotedCount());
        sb2.append(", Voters by Non Aadhaar:");
        sb2.append(this.db.getNonAadhaarVotedCount());
        textView7.setText(sb2.toString());
        TextView textView8 = this.txtNumMaleVoted;
        StringBuilder sb3 = new StringBuilder();
        sb3.append("Male voters ");
        sb3.append(this.db.getNumberMalesVoted());
        textView8.setText(sb3.toString());
        this.txtNumMaleVoted.setVisibility(8);
        this.list = this.db.getAllTransactionTableData();
        List<VotingHistoryModel> list = this.list;
        if (list != null && !list.isEmpty() && this.list.size() > 0) {
            setRecyclerView(this.list);
        }
    }

    @Override // androidx.activity.ComponentActivity, android.app.Activity
    public void onBackPressed() {
        startActivity(new Intent(this, ListUserActivity.class));
        finish();
        super.onBackPressed();
    }

    @Override // com.example.aadhaarfpoffline.tatvik.adapter.VotingHistoryAdapter.OnItemClickListener
    public void onItemClick3(int position, String slnoinward) {
        if (this.list.get(position).getVoted() == null || this.list.get(position).getVoted().isEmpty() || this.list.get(position).getVoted().length() < 1) {
            Toast.makeText(getApplicationContext(), "Voted fieled empty", 1).show();
        }
        if (this.list.get(position).getVoted().equalsIgnoreCase(ExifInterface.GPS_MEASUREMENT_2D)) {
            String user_id = this.list.get(position).getMATCHED_USER_ID();
            if (user_id == null || user_id.isEmpty() || user_id.length() == 0) {
                Toast.makeText(getApplicationContext(), "Matched User Id is empty", 1).show();
            } else if (user_id.equalsIgnoreCase("REJECTED_BY_AADHAAR")) {
                Toast.makeText(getApplicationContext(), "Rejected by Aadhaar", 1).show();
            } else if (isNetworkAvailable()) {
                getVoterByUserId(user_id, position);
            } else {
                useridLocalsearch(user_id, position);
            }
        } else {
            Toast.makeText(getApplicationContext(), "This voter was not rejected", 1).show();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void useridLocalsearch(String user_id, int position) {
        if (!user_id.contains(this.userAuth.getPanchayatId() + "_" + this.userAuth.getWardNo() + "_" + this.userAuth.getBoothNo())) {
            Toast.makeText(getApplicationContext(), "Please get online to get data", 1).show();
            return;
        }
        Toast.makeText(getApplicationContext(), "Offline data", 1).show();
        String[] words = user_id.split("_");
        if (words != null && words.length == 4) {
            Log.d(BuildConfig.BUILD_TYPE, "" + words.length);
            String matchslnoinward = words[words.length - 1];
            VoterDataNewModel Voter = this.db.getVoterBySlNoInWard(matchslnoinward);
            popupFailedOffline("Matched against " + matchslnoinward, Voter, position);
        } else if (user_id.equalsIgnoreCase("AADHAAR_ALREADY_EXISTS")) {
            Toast.makeText(getApplicationContext(), "Aadhaar number already existed in database", 1).show();
        }
    }

    private void getTransTableInList() {
    }

    private void getVoterByUserId(final String user_id, final int position) {
        Context applicationContext = getApplicationContext();
        Toast.makeText(applicationContext, "Userid from server" + user_id, 1).show();
        HashMap<String, String> map = new HashMap<>();
        map.put("matchuser", user_id);
        ((GetDataService) RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class)).getVoterByUserIdTransTable(map).enqueue(new Callback<VoterByUserIdGetResponse>() { // from class: com.example.aadhaarfpoffline.tatvik.activity.VotingStatusActivity.1
            @Override // retrofit2.Callback
            public void onResponse(Call<VoterByUserIdGetResponse> call, Response<VoterByUserIdGetResponse> response) {
                if (response == null || !response.isSuccessful() || response.body() == null || !response.body().getUserIdExists().booleanValue()) {
                    VotingStatusActivity.this.useridLocalsearch(user_id, position);
                    return;
                }
                VotingStatusActivity.this.popupFailed("Rejected", response.body().getVoter(), user_id.replace("_", "/"));
            }

            @Override // retrofit2.Callback
            public void onFailure(Call<VoterByUserIdGetResponse> call, Throwable t) {
                VotingStatusActivity.this.useridLocalsearch(user_id, position);
            }
        });
    }

    private void setRecyclerView(List<VotingHistoryModel> list) {
        this.adapter = new VotingHistoryAdapter(this, this, list);
        this.recyclerView.setAdapter(this.adapter);
        this.recyclerView.setLayoutManager(new LinearLayoutManager(this, 1, false));
    }

    private String getBoothInFormat(String boothnumstr) {
        Integer boothnum = Integer.valueOf(Integer.parseInt(boothnumstr));
        if (boothnum.intValue() <= 1000) {
            return String.valueOf(boothnum);
        }
        if (boothnum.intValue() > 1000 && boothnum.intValue() <= 2000) {
            return String.valueOf((boothnum.intValue() - 1000) + "क");
        } else if (boothnum.intValue() > 2000 && boothnum.intValue() <= 3000) {
            return String.valueOf((boothnum.intValue() - 2000) + "ख");
        } else if (boothnum.intValue() > 3000 && boothnum.intValue() <= 4000) {
            return String.valueOf((boothnum.intValue() - PathInterpolatorCompat.MAX_NUM_POINTS) + "ग");
        } else if (boothnum.intValue() > 4000 && boothnum.intValue() <= 5000) {
            return String.valueOf((boothnum.intValue() - 4000) + "घ");
        } else if (boothnum.intValue() > 5000 && boothnum.intValue() <= 6000) {
            return String.valueOf((boothnum.intValue() - 5000) + "ङ");
        } else if (boothnum.intValue() <= 6000 || boothnum.intValue() > 7000) {
            return "";
        } else {
            return String.valueOf((boothnum.intValue() - 6000) + "च");
        }
    }

    private void popupFailedOffline(String failedmessage, VoterDataNewModel voter, int position) {
        String user_id = this.list.get(position).getMATCHED_USER_ID();
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        System.out.println("popup2");
        alertDialogBuilder.setTitle(this.resources.getString(R.string.aadhaar_authentication));
        alertDialogBuilder.setIcon(R.drawable.wrong_icon_trp);
        System.out.println("popup4");
        alertDialogBuilder.setPositiveButton(17039379, new DialogInterface.OnClickListener() { // from class: com.example.aadhaarfpoffline.tatvik.activity.VotingStatusActivity.2
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        View dialogView = getLayoutInflater().inflate(R.layout.alert_custom_layout, (ViewGroup) null);
        alertDialogBuilder.setView(dialogView);
        SimpleDraweeView matchUserImage = (SimpleDraweeView) dialogView.findViewById(R.id.usermatchimage);
        ImageView imageView = (ImageView) dialogView.findViewById(R.id.image_1);
        imageView.setVisibility(0);
        imageView.setImageResource(R.drawable.wrong_icon_trp);
        TextView messageText = (TextView) dialogView.findViewById(R.id.match_message);
        TextView messageVoterOnNot = (TextView) dialogView.findViewById(R.id.message);
        String voterimage = this.list.get(position).getMATCHED_ID_DOCUMENT_IMAGE();
        File imgFile = new File("/sdcard/" + Const.PublicImageName + "/" + voterimage);
        if (imgFile.exists()) {
            Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
            matchUserImage.setVisibility(0);
            matchUserImage.setImageBitmap(myBitmap);
        } else {
            Uri uri1 = Uri.parse("http://election.phoneme.in:9090/VoterAuthenticationapi/getimagesthumb?file=" + voterimage);
            matchUserImage.setVisibility(0);
            matchUserImage.setImageURI(uri1);
        }
        String name = this.resources.getString(R.string.name) + ":";
        if (this.lan.equalsIgnoreCase("en")) {
            if (voter.getFM_NAME_EN() != null) {
                name = name + voter.getFM_NAME_EN();
            }
            if (voter.getLASTNAME_EN() != null) {
                name = name + " " + voter.getLASTNAME_EN();
            }
        } else {
            if (voter.getFM_NAME_V1() != null) {
                name = name + voter.getFM_NAME_V1();
            }
            if (voter.getLASTNAME_V1() != null) {
                name = name + " " + voter.getLASTNAME_V1();
            }
        }
        String age = this.resources.getString(R.string.age) + ":" + voter.getAge();
        String wardnum = this.resources.getString(R.string.ward_no) + ":" + voter.getWardNo();
        String[] words = user_id.split("_");
        String matchslnoinward = words[words.length - 1];
        String votingdate = this.resources.getString(R.string.voting_date) + ":" + this.db.getDateFromSlNoinWard(matchslnoinward);
        messageText.setText(name + IOUtils.LINE_SEPARATOR_UNIX + (this.resources.getString(R.string.gender) + ":" + voter.getGENDER()) + IOUtils.LINE_SEPARATOR_UNIX + age + IOUtils.LINE_SEPARATOR_UNIX + wardnum + IOUtils.LINE_SEPARATOR_UNIX + votingdate + IOUtils.LINE_SEPARATOR_UNIX + (this.resources.getString(R.string.panchayat_id) + "/" + this.resources.getString(R.string.ward_no) + "/" + this.resources.getString(R.string.booth_no_text) + ":" + voter.getPanchayatID() + "/" + voter.getWardNo() + "/" + voter.getBoothNo()));
        String finalmessage = this.resources.getString(R.string.u_cannot_vote_text);
        StringBuilder sb = new StringBuilder();
        sb.append(finalmessage);
        sb.append(IOUtils.LINE_SEPARATOR_UNIX);
        sb.append(this.resources.getString(R.string.fingerprintrecord_match_text));
        sb.append(voter.getEPIC_NO());
        sb.toString();
        messageVoterOnNot.setVisibility(0);
        messageVoterOnNot.setText(failedmessage);
        AlertDialog alertDialog = alertDialogBuilder.create();
        System.out.println("popup5");
        alertDialog.show();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void popupFailed(String failedmessage, VoterDataNewModel voter, String boothdata) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        System.out.println("popup2");
        alertDialogBuilder.setTitle(this.resources.getString(R.string.aadhaar_authentication));
        alertDialogBuilder.setIcon(R.drawable.wrong_icon_trp);
        System.out.println("popup4");
        alertDialogBuilder.setPositiveButton(17039379, new DialogInterface.OnClickListener() { // from class: com.example.aadhaarfpoffline.tatvik.activity.VotingStatusActivity.3
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        View dialogView = getLayoutInflater().inflate(R.layout.alert_custom_layout, (ViewGroup) null);
        alertDialogBuilder.setView(dialogView);
        String imageurl = "";
        SimpleDraweeView matchUserImage = (SimpleDraweeView) dialogView.findViewById(R.id.usermatchimage);
        ImageView imageView = (ImageView) dialogView.findViewById(R.id.image_1);
        imageView.setVisibility(0);
        imageView.setImageResource(R.drawable.wrong_icon_trp);
        TextView messageText = (TextView) dialogView.findViewById(R.id.match_message);
        TextView messageVoterOnNot = (TextView) dialogView.findViewById(R.id.message);
        if (voter.getID_DOCUMENT_IMAGE() != null) {
            imageurl = voter.getID_DOCUMENT_IMAGE();
            Uri uri1 = Uri.parse(imageurl);
            matchUserImage.setVisibility(0);
            matchUserImage.setImageURI(uri1);
        }
        String name = this.resources.getString(R.string.name) + ":";
        if (this.lan.equalsIgnoreCase("en")) {
            if (voter.getFM_NAME_EN() != null) {
                name = name + voter.getFM_NAME_EN();
            }
            if (voter.getLASTNAME_EN() != null) {
                name = name + " " + voter.getLASTNAME_EN();
            }
        } else {
            if (voter.getFM_NAME_V1() != null) {
                name = name + voter.getFM_NAME_V1();
            }
            if (voter.getLASTNAME_V1() != null) {
                name = name + " " + voter.getLASTNAME_V1();
            }
        }
        voter.getUSER_ID().replace('_', IOUtils.DIR_SEPARATOR_UNIX);
        String age = this.resources.getString(R.string.age) + ":" + voter.getAge();
        String wardnum = this.resources.getString(R.string.ward_no) + ":" + voter.getWardNo();
        String votingdate = this.resources.getString(R.string.voting_date) + ":" + voter.getVOTING_DATE();
        String str = "Voterid:" + voter.getEPIC_NO();
        messageText.setText(name + IOUtils.LINE_SEPARATOR_UNIX + (this.resources.getString(R.string.gender) + ":" + voter.getGENDER()) + IOUtils.LINE_SEPARATOR_UNIX + age + IOUtils.LINE_SEPARATOR_UNIX + wardnum + IOUtils.LINE_SEPARATOR_UNIX + votingdate + IOUtils.LINE_SEPARATOR_UNIX + (this.resources.getString(R.string.panchayat_id) + "/" + this.resources.getString(R.string.ward_no) + "/" + this.resources.getString(R.string.booth_no_text) + ":" + boothdata));
        String finalmessage = this.resources.getString(R.string.u_cannot_vote_text);
        StringBuilder sb = new StringBuilder();
        sb.append(finalmessage);
        sb.append(IOUtils.LINE_SEPARATOR_UNIX);
        sb.append(this.resources.getString(R.string.fingerprintrecord_match_text));
        sb.append(voter.getEPIC_NO());
        sb.toString();
        messageVoterOnNot.setVisibility(0);
        messageVoterOnNot.setText(failedmessage);
        AlertDialog alertDialog = alertDialogBuilder.create();
        System.out.println("popup5");
        alertDialog.show();
    }

    private boolean isNetworkAvailable() {
        return ((ConnectivityManager) getSystemService("connectivity")).getActiveNetworkInfo() != null;
    }
}
