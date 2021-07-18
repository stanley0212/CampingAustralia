package com.luvtas.campingau.CallBack;

import com.luvtas.campingau.Model.MultiImageModel;

import java.util.List;

public interface MultiImageCallbackListener {
    void onMultiLoadSuccess(List<MultiImageModel> multiImageModels);
    void onMultiLoadFailed(String message);
}
