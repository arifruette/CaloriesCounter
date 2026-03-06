package ru.ari.caloriescounter.feature.diary.presentation

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import ru.ari.caloriescounter.R

@AndroidEntryPoint
class DiaryFragment : Fragment(R.layout.fragment_diary) {

    private val viewModel: DiaryViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.onIntent(DiaryIntent.ScreenOpened)
    }
}
