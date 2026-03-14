package ru.ari.caloriescounter.feature.diary.presentation

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import ru.ari.caloriescounter.R
import ru.ari.caloriescounter.databinding.FragmentDiaryBinding
import ru.ari.caloriescounter.feature.diary.domain.model.MealType

@AndroidEntryPoint
class DiaryFragment : Fragment(R.layout.fragment_diary) {

    private val viewModel: DiaryViewModel by viewModels()
    private var _binding: FragmentDiaryBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentDiaryBinding.bind(view)

        bindClicks()
        collectState()
        collectEffects()

        viewModel.onIntent(DiaryIntent.ScreenOpened)
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    private fun bindClicks() {
        binding.cardBreakfast.setOnClickListener {
            viewModel.onIntent(DiaryIntent.MealClicked(MealType.BREAKFAST))
        }
        binding.cardLunch.setOnClickListener {
            viewModel.onIntent(DiaryIntent.MealClicked(MealType.LUNCH))
        }
        binding.cardSnack.setOnClickListener {
            viewModel.onIntent(DiaryIntent.MealClicked(MealType.SNACK))
        }
    }

    private fun collectState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.state.collect { state ->
                    binding.progressBar.isVisible = state.isLoading
                    state.mealCards.forEach { card ->
                        when (card.mealType) {
                            MealType.BREAKFAST -> {
                                binding.textBreakfastTitle.text = card.title
                                binding.textBreakfastSubtitle.text = card.subtitle
                            }

                            MealType.LUNCH -> {
                                binding.textLunchTitle.text = card.title
                                binding.textLunchSubtitle.text = card.subtitle
                            }

                            MealType.SNACK -> {
                                binding.textSnackTitle.text = card.title
                                binding.textSnackSubtitle.text = card.subtitle
                            }
                        }
                    }
                }
            }
        }
    }

    private fun collectEffects() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.effects.collect { effect ->
                    when (effect) {
                        is DiaryEffect.NavigateToMealProducts -> {
                            findNavController().navigate(
                                R.id.action_diaryFragment_to_mealProductsPlaceholderFragment,
                                bundleOf(MEAL_TYPE_ARG to effect.mealType.name),
                            )
                        }
                    }
                }
            }
        }
    }

    companion object {
        const val MEAL_TYPE_ARG = "mealType"
    }
}

