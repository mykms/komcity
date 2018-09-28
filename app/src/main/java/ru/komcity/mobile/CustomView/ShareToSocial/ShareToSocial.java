package ru.komcity.mobile.CustomView.ShareToSocial;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.v4.content.FileProvider;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;
import java.io.File;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import ru.komcity.mobile.R;
import ru.komcity.mobile.base.Utils;

public class ShareToSocial extends RelativeLayout {
    private Intent shareIntent = new Intent(Intent.ACTION_SEND);
    private String imgPath = "";
    private String textForShare = "";
    @BindView(R.id.group_messenger) public LinearLayout groupMessenger;
    @BindView(R.id.img_share) public ImageView imgShare;

    public ShareToSocial(Context context, AttributeSet attrs) {
        super(context, attrs);

        init(attrs);
    }

    private void init(AttributeSet attrs) {
        LayoutInflater.from(getContext()).inflate(R.layout.view_share, this);
        ButterKnife.bind(this);

        groupMessenger.setVisibility(GONE); // Default - invisible
    }

    /**
     * Установка размером кастомной вьюхи
     * @param size Размер в dp
     */
    public void setViewSize(int size) {
        if (size < 0)
            size = 1;
        if (size > 0) {
            if (imgShare != null) {
                ViewGroup.LayoutParams params = imgShare.getLayoutParams();
                int newsize = (int)convertDptoPX(size);
                params.height = newsize;
                params.width = newsize;
                imgShare.setLayoutParams(params);
            }
        }
    }

    /**
     * Устанавливает полный путь к изображению, которым нужно поделиться
     * @param img_path Полный путь к изображению
     */
    public void setImagePathToShare(String img_path) {
        if (img_path == null)
            img_path = "";
        imgPath = img_path;
    }

    /**
     * Преобразование dp в px
     * @param valueDP Значение в dp
     * @return Возвращает значение в px
     */
    private float convertDptoPX(int valueDP) {
        if (valueDP < 0)
            valueDP = 0;
        Resources r = getResources();
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, valueDP, r.getDisplayMetrics());
    }

    /**
     * Проверяет установлено ли приложение с указанным именем пакета
     * @param context Контекст
     * @param appName Имя пакета для поиска
     * @return Результат поиска. true - если такой пакет есть, иначе false
     */
    private boolean isAppAvailable(Context context, String appName) {
        PackageManager pm = context.getPackageManager();
        try {
            pm.getPackageInfo(appName, PackageManager.GET_ACTIVITIES);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }

    /**
     * Устанавливает рисунок в виде bmp-данных, чтобы им поделиться
     * @param bmp Рисунок, представленный Bitmap данными
     * @param fName Путь для сохранения. Если null, то по умолчанию
     */
    public void setBitmapToShare(Activity activityForPermission, Bitmap bmp, String fName) {
        if (bmp == null) {
            // Создаем рисунок по умолчанию (логотип)
            bmp = BitmapFactory.decodeResource(getResources(), R.drawable.vector_ic_news);

            Matrix matrix = new Matrix();
            matrix.postScale(10, 15);
            matrix.postRotate(45);
        }

        Utils utils = new Utils();
        if (fName == null) {
            fName = "img.jpg";
        }
        if (utils.saveImageLocal(activityForPermission, bmp, fName)) {
            imgPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) +
                    File.separator +
                    fName;
        }
    }

    public void setTextForShare(String text) {
        if (text == null)
            text = "";
        textForShare = text;
    }

    private void shareToApp() {
        String type = "image/*";
        String typeText = "text/plain";

        File media = new File(imgPath);

        try {
            shareIntent.setAction(Intent.ACTION_SEND);
            shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            shareIntent.setType(type);
            if (media.exists()) {
                Uri uri = Uri.fromFile(media);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    uri = FileProvider.getUriForFile(getContext(), getContext().getPackageName() + ".provider", media);
                }
                shareIntent.putExtra(Intent.EXTRA_STREAM, uri);
            }
            shareIntent.putExtra(Intent.EXTRA_TEXT, textForShare);
            getContext().startActivity(Intent.createChooser(shareIntent, "Поделиться в"));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void setPackageToShare(String packageName) {
        if (isAppAvailable(getContext(), packageName)) {
            shareIntent.setPackage(packageName);
            shareToApp();
        } else {
            //Уведомление, что не установлено приложение
            Toast.makeText(getContext(),
                    "Выбранное приложение не установлено",
                    Toast.LENGTH_SHORT).show();
        }
    }

    @OnClick(R.id.img_share)
    public void OnShareClick() {
        int resId = R.drawable.vector_ic_arrow_back;
        if (groupMessenger.getVisibility() == GONE) {
            resId = R.drawable.vector_ic_arrow_next;
            groupMessenger.setVisibility(VISIBLE);
        } else {
            resId = R.drawable.vector_ic_arrow_back;
            groupMessenger.setVisibility(GONE);
        }
        imgShare.setImageDrawable(getResources().getDrawable(resId));
    }

    @OnClick(R.id.img_instagram)
    public void OnClick_instagram() {
        setPackageToShare("com.instagram.android");
    }

    @OnClick(R.id.img_facebook)
    public void OnClick_facebook() {
        setPackageToShare("com.facebook.katana");
    }

    @OnClick(R.id.img_twitter)
    public void OnClick_twitter() {
        setPackageToShare("com.twitter.android");
    }

    @OnClick(R.id.img_vkontakte)
    public void OnClick_vkontakte() {
        setPackageToShare("com.vkontakte.android");
    }

    @OnClick(R.id.img_whatsapp)
    public void OnClick_whatsapp() {
        setPackageToShare("com.whatsapp");
    }

    @OnClick(R.id.img_telegram)
    public void OnClick_telegram() {
        setPackageToShare("org.telegram.messenger");
    }
}
