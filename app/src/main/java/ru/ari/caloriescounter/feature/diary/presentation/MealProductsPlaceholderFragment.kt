package ru.ari.caloriescounter.feature.diary.presentation

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import dagger.hilt.android.AndroidEntryPoint
import ru.ari.caloriescounter.R
import ru.ari.caloriescounter.databinding.FragmentMealProductsPlaceholderBinding
import ru.ari.caloriescounter.feature.diary.domain.model.MealType

@AndroidEntryPoint
class MealProductsPlaceholderFragment : Fragment(R.layout.fragment_meal_products_placeholder) {

    private var _binding: FragmentMealProductsPlaceholderBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentMealProductsPlaceholderBinding.bind(view)

        val mealType = arguments?.getString(DiaryFragment.MEAL_TYPE_ARG)
            ?.let { runCatching { MealType.valueOf(it) }.getOrNull() }
            ?: MealType.BREAKFAST

        binding.textTitle.text = getString(R.string.screen_meal_products_title, mealType.toTitle())
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}

private fun MealType.toTitle(): String = when (this) {
    MealType.BREAKFAST -> "Завтрак"
    MealType.LUNCH -> "Обед"
    MealType.SNACK -> "Перекус"
}

