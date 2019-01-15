package co.ajsf.tuner.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import co.ajsf.tuner.viewmodel.TunerViewModel
import co.ajsf.tuner.viewmodel.ViewModelFactory
import org.kodein.di.Kodein
import org.kodein.di.direct
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider
import org.kodein.di.generic.singleton

fun tunerActivityModule() = Kodein
    .Module("tunerActivityModule") {
        bind<ViewModel>(tag = TunerViewModel::class.java.simpleName) with provider {
            TunerViewModel(instance())
        }
        bind<ViewModelProvider.Factory>() with singleton { ViewModelFactory(kodein.direct) }
    }