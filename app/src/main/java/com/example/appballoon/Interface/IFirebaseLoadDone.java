package com.example.appballoon.Interface;

import com.example.appballoon.Class.Balloon;

import java.util.List;

public interface IFirebaseLoadDone {
    void onFireBaseLoadSuccess (List<Balloon> balloonList);
    void onFireBaseLoadFailed (String message);
}
