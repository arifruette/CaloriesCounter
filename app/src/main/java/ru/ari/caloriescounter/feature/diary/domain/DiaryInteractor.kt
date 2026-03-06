package ru.ari.caloriescounter.feature.diary.domain

import javax.inject.Inject

interface DiaryInteractor

class DiaryInteractorImpl @Inject constructor(
    private val repository: DiaryRepository,
) : DiaryInteractor
