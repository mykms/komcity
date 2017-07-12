package testapp.komcity.ru.myapplicationetalon;

import com.hannesdorfmann.mosby3.mvp.MvpView;

public interface IPresNews extends MvpView {
    public void showProgress();
    public void hideProgress();
    public void loadNews();
    public void showError();
}
