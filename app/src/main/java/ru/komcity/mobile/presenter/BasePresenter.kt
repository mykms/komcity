package ru.komcity.mobile.presenter

import moxy.InjectViewState
import moxy.MvpPresenter
import moxy.MvpView

/**
 * Created by Aleksei Kholoimov on 2020-02-27
 * <p>
 *
 */
@InjectViewState
open class BasePresenter<View : MvpView> : MvpPresenter<View>() {

}