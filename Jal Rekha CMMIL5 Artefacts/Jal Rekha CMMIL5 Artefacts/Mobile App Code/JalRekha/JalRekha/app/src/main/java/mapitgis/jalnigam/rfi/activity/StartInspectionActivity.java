package mapitgis.jalnigam.rfi.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import mapitgis.jalnigamk.nirmal.view.FormKeyValueText;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import mapitgis.jalnigam.R;
import mapitgis.jalnigam.rfi.viewmodel.ContractorViewModel;
import mapitgis.jalnigam.room.table.InspectionRequestTable;
import mapitgis.jalnigam.room.table.PipeLineTable;

public class StartInspectionActivity extends AppCompatActivity {

    private ImageView backImageView;
    private TextView titleTextView;

    private Button btnNext, btnViewEnclosure;

    private TextView requestIdTv, requestDateTv, schemeNameTv, componentTypeTv;
    private TextView pointNameTv, applicationTypeTv, blockTv, gramTv, villageTv;
    private TextView locationTv, descriptionTv, inspectionDateTv, statusTv, remarkTv;

    private InspectionRequestTable history;

    private LinearLayout layoutPipelineDetail;

    private ContractorViewModel contractorViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //EdgeToEdge.enable(this);
        setContentView(R.layout.activity_start_inspection);
//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
//            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
//            return insets;
//        });

        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));

        history = (InspectionRequestTable) getIntent()
                .getSerializableExtra("detail");

        contractorViewModel = new ViewModelProvider(this).get(ContractorViewModel.class);

        backImageView = findViewById(R.id.tool_bar_navigation_icon_image_view);
        titleTextView = findViewById(R.id.toolbar_title_text_view);
        backImageView.setImageResource(R.drawable.ic_arrow_back_black_24dp);
        titleTextView.setText("Inspection");
        backImageView.setOnClickListener(v -> finish());

        requestIdTv = findViewById(R.id.start_inspection_request_id_tv);
        requestDateTv = findViewById(R.id.start_inspection_request_date_tv);
        schemeNameTv = findViewById(R.id.start_inspection_scheme_name_tv);
        componentTypeTv = findViewById(R.id.start_inspection_component_type_tv);
        pointNameTv = findViewById(R.id.start_inspection_point_name_tv);
        applicationTypeTv = findViewById(R.id.start_inspection_application_type_tv);
        blockTv = findViewById(R.id.start_inspection_block_tv);
        gramTv = findViewById(R.id.start_inspection_gram_tv);
        villageTv = findViewById(R.id.start_inspection_village_tv);
        locationTv = findViewById(R.id.start_inspecton_location_tv);
        descriptionTv = findViewById(R.id.start_inspection_description_tv);
        inspectionDateTv = findViewById(R.id.start_inspection_date_tv);
        statusTv = findViewById(R.id.start_inspection_status_tv);
        remarkTv = findViewById(R.id.start_inspection_remark_tv);

        requestIdTv.setText(history.getRfiId());
        schemeNameTv.setText(history.getSchemeName());
        componentTypeTv.setText(history.getComponentName());
        pointNameTv.setText(history.getPointName());
        applicationTypeTv.setText(history.getApplicationName());
        descriptionTv.setText(history.getDescription());
        locationTv.setText(history.getLocation());
        inspectionDateTv.setText(history.getInspectionDate());
        statusTv.setText(history.getStatusName());
        remarkTv.setText("");
        requestDateTv.setText(history.getInsertDate());
        villageTv.setText(history.getVillageName());

        btnNext = findViewById(R.id.btn_next_inspection_start);
        btnNext.setOnClickListener(v ->
                startActivity(new Intent(this,
                        SubmitInspectionActivity.class).putExtra("detail", history)));

        btnViewEnclosure = findViewById(R.id.btn_start_ins_view_enclosure);
        btnViewEnclosure.setOnClickListener(v -> photos());

        layoutPipelineDetail = findViewById(R.id.layout_pipeline_detail);
        if(!history.getPipeNo().isEmpty() && !history.getPipeNo().equals("0")){
            layoutPipelineDetail.setVisibility(View.VISIBLE);
            contractorViewModel.getPineLineDetail(
                    history.getSchemeId(),
                    Integer.parseInt(history.getComponentId()),
                    history.getMbrOhtSurveyId(),
                    history.getPipeNo()).observe(this, this::showPipInfo);

        }
    }

    private void photos() {
        String[] itemsArray = history.getImages().split(",");

        List<String> itemsList = Arrays.asList(itemsArray);
        ArrayList<String> updatedItemsList = addDefaultValueToList(itemsList);

        startActivity(new Intent(this, FullImageActivity.class)
                .putStringArrayListExtra("list", updatedItemsList));
    }

    private ArrayList<String> addDefaultValueToList(List<String> list) {
        ArrayList<String> updatedList = new ArrayList<>();
        for (String item : list) {
//            updatedList.add(defaultValue + item);
            updatedList.add(item.trim());
        }
        return updatedList;
    }



    private void showPipInfo(PipeLineTable pipeInfo){
        if(pipeInfo!=null){
            ((FormKeyValueText)findViewById(R.id.tv_pipeno)).setValue(pipeInfo.getPipNo());
            ((FormKeyValueText)findViewById(R.id.tv_zone)).setValue(pipeInfo.getZoneNo()==null ? "" : pipeInfo.getZoneNo());
            ((FormKeyValueText)findViewById(R.id.tv_startno)).setValue(pipeInfo.getStartNode());
            ((FormKeyValueText)findViewById(R.id.tv_stop_no)).setValue(pipeInfo.getStopNode());
            ((FormKeyValueText)findViewById(R.id.tv_diameter)).setValue(pipeInfo.getDia());
            ((FormKeyValueText)findViewById(R.id.tv_length)).setValue(pipeInfo.getLength());
            ((FormKeyValueText)findViewById(R.id.tv_material)).setValue(pipeInfo.getMaterial());
            ((FormKeyValueText)findViewById(R.id.tv_thickness)).setValue(pipeInfo.getThickness());

            ((FormKeyValueText)findViewById(R.id.tv_pipe_stretch)).setValue(history.getLengthSlot().equals("0")?"":history.getLengthSlot());
            ((FormKeyValueText)findViewById(R.id.tv_survey_uid)).setValue(history.getMbrOhtSurveyId().equals("0")?"":history.getLengthSlot());
        }
    }
}