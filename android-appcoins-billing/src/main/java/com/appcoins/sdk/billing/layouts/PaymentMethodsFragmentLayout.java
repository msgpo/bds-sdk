package com.appcoins.sdk.billing.layouts;

import android.app.Activity;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.StateListDrawable;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.appcoins.sdk.billing.BuyItemProperties;
import com.appcoins.sdk.billing.payasguest.PaymentMethodsFragment;
import com.appcoins.sdk.billing.utils.PaymentErrorViewLayout;
import java.io.IOException;
import java.io.InputStream;

import static com.appcoins.sdk.billing.utils.LayoutUtils.BUTTONS_RESOURCE_PATH;
import static com.appcoins.sdk.billing.utils.LayoutUtils.IMAGES_RESOURCE_PATH;
import static com.appcoins.sdk.billing.utils.LayoutUtils.dpToPx;
import static com.appcoins.sdk.billing.utils.LayoutUtils.generateRandomId;
import static com.appcoins.sdk.billing.utils.LayoutUtils.mapDisplayMetrics;
import static com.appcoins.sdk.billing.utils.LayoutUtils.setConstraint;
import static com.appcoins.sdk.billing.utils.LayoutUtils.setMargins;
import static com.appcoins.sdk.billing.utils.LayoutUtils.setPadding;

public class PaymentMethodsFragmentLayout {

  private int installMainTextId;
  private int installPaypalId;
  private int installCreditCardId;
  private int installRadioButtonId;
  private int paypalImageId;
  private int creditCardImageId;
  private int payAsGuestTextId;
  private int headerId;
  private int paymentMethodsId;
  private int paymentMethodsHeaderId;
  private int fiatPriceViewId;
  private int appcPriceViewId;
  private int appNameId;
  private int appIconId;
  private Activity activity;
  private int orientation;
  private BuyItemProperties buyItemProperties;
  private TextView fiatPriceView;
  private TextView appcPriceView;
  private RadioButton creditCardRadioButton;
  private RadioButton paypalRadioButton;
  private RadioButton installRadioButton;
  private String densityPath;
  private Button cancelButton;
  private Button positiveButton;
  private ViewGroup creditCardWrapperLayout;
  private ViewGroup paypalWrapperLayout;
  private ViewGroup installWrapperLayout;
  private GradientDrawable selectedBackground;
  private GradientDrawable defaultBackground;
  private ViewGroup paymentMethodsLayout;
  private ViewGroup errorView;
  private ViewGroup dialogLayout;
  private TextView installSecondaryText;
  private ViewGroup intentLoadingView;
  private PaymentErrorViewLayout paymentErrorViewLayout;

  public PaymentMethodsFragmentLayout(Activity activity, int orientation,
      BuyItemProperties buyItemProperties) {
    this.activity = activity;
    this.orientation = orientation;
    this.buyItemProperties = buyItemProperties;
  }

  public View build() {
    DisplayMetrics displayMetrics = new DisplayMetrics();
    activity.getWindowManager()
        .getDefaultDisplay()
        .getMetrics(displayMetrics);
    densityPath = mapDisplayMetrics(displayMetrics);

    RelativeLayout mainLayout = buildMainLayout();

    paymentErrorViewLayout = new PaymentErrorViewLayout(activity, orientation);
    errorView = paymentErrorViewLayout.buildErrorView();
    errorView.setVisibility(View.INVISIBLE);

    intentLoadingView = buildIntentLoadingView();

    dialogLayout = buildDialogLayout();
    dialogLayout.setVisibility(View.GONE);

    mainLayout.addView(dialogLayout);
    mainLayout.addView(errorView);
    mainLayout.addView(intentLoadingView);
    return mainLayout;
  }

  private RelativeLayout buildIntentLoadingView() {
    RelativeLayout relativeLayout = new RelativeLayout(activity);

    RelativeLayout.LayoutParams layoutParams =
        new RelativeLayout.LayoutParams(dpToPx(262), dpToPx(254));
    layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);
    relativeLayout.setLayoutParams(layoutParams);

    GradientDrawable gradientDrawable = new GradientDrawable();
    gradientDrawable.setColor(Color.WHITE);
    gradientDrawable.setCornerRadius(dpToPx(8));
    relativeLayout.setBackground(gradientDrawable);

    ProgressBar progressBar = buildProgressBar();

    relativeLayout.addView(progressBar);
    return relativeLayout;
  }

  private RelativeLayout buildPaymentMethodsHeaderLayout() {
    RelativeLayout paymentMethodHeaderLayout = new RelativeLayout(activity);
    paymentMethodHeaderLayout.setLayoutParams(
        new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT));
    paymentMethodsHeaderId = generateRandomId();
    paymentMethodHeaderLayout.setId(paymentMethodsHeaderId);
    Drawable icon = null;
    String appName = "";
    PackageManager packageManager = activity.getApplicationContext()
        .getPackageManager();
    try {
      icon = packageManager.getApplicationIcon(buyItemProperties.getPackageName());
      ApplicationInfo applicationInfo =
          packageManager.getApplicationInfo(buyItemProperties.getPackageName(), 0);
      appName = packageManager.getApplicationLabel(applicationInfo)
          .toString();
    } catch (Exception e) {
      e.printStackTrace();
    }
    fiatPriceView = createFiatPriceView();
    appcPriceView = createAppcPriceView();
    ImageView iconImageView = createAppIconLayout(icon);
    TextView appNameView = createAppNameLayout(appName);
    TextView skuView = createSkuLayout(buyItemProperties.getSku());

    paymentMethodHeaderLayout.addView(iconImageView);
    paymentMethodHeaderLayout.addView(appNameView);
    paymentMethodHeaderLayout.addView(skuView);
    paymentMethodHeaderLayout.addView(fiatPriceView);
    paymentMethodHeaderLayout.addView(appcPriceView);
    return paymentMethodHeaderLayout;
  }

  private TextView createAppcPriceView() {
    TextView textView = new TextView(activity);
    appcPriceViewId = generateRandomId();
    textView.setId(appcPriceViewId);
    RelativeLayout.LayoutParams layoutParams =
        new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT);
    layoutParams.addRule(RelativeLayout.BELOW, fiatPriceViewId);
    setConstraint(layoutParams, RelativeLayout.ALIGN_PARENT_RIGHT);
    setMargins(layoutParams, 0, 0, 16, 0);
    textView.setTextColor(Color.parseColor("#828282"));
    textView.setTextSize(12);
    textView.setLayoutParams(layoutParams);
    return textView;
  }

  private TextView createFiatPriceView() {
    TextView textView = new TextView(activity);
    fiatPriceViewId = generateRandomId();
    textView.setId(fiatPriceViewId);
    RelativeLayout.LayoutParams layoutParams =
        new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT);
    setConstraint(layoutParams, RelativeLayout.ALIGN_PARENT_RIGHT);
    setMargins(layoutParams, 0, 17, 16, 0);
    textView.setTextColor(Color.parseColor("#000000"));
    textView.setTextSize(15);
    textView.setTypeface(Typeface.create("sans-serif-medium", Typeface.NORMAL));
    textView.setLayoutParams(layoutParams);
    return textView;
  }

  private TextView createSkuLayout(String sku) {
    TextView textView = new TextView(activity);
    RelativeLayout.LayoutParams layoutParams =
        new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT);
    layoutParams.addRule(RelativeLayout.BELOW, appNameId);
    setConstraint(layoutParams, RelativeLayout.LEFT_OF, appcPriceViewId);
    setConstraint(layoutParams, RelativeLayout.RIGHT_OF, appIconId);
    setMargins(layoutParams, 10, 0, 12, 0);
    textView.setEllipsize(TextUtils.TruncateAt.END);
    textView.setMaxLines(1);
    textView.setTextColor(Color.parseColor("#8a000000"));
    textView.setTextSize(12);
    textView.setText(sku);
    textView.setLayoutParams(layoutParams);
    return textView;
  }

  private TextView createAppNameLayout(String appName) {
    TextView textView = new TextView(activity);
    appNameId = generateRandomId();
    textView.setId(appNameId);
    RelativeLayout.LayoutParams layoutParams =
        new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT);
    setConstraint(layoutParams, RelativeLayout.LEFT_OF, fiatPriceViewId);
    setConstraint(layoutParams, RelativeLayout.RIGHT_OF, appIconId);
    setMargins(layoutParams, 10, 15, 12, 0);
    textView.setEllipsize(TextUtils.TruncateAt.END);
    textView.setMaxLines(1);
    textView.setTextColor(Color.parseColor("#de000000"));
    textView.setTextSize(16);
    textView.setText(appName);
    textView.setLayoutParams(layoutParams);
    return textView;
  }

  private ImageView createAppIconLayout(Drawable icon) {
    ImageView imageView = new ImageView(activity);
    appIconId = generateRandomId();
    imageView.setId(appIconId);
    if (icon != null) {
      imageView.setImageDrawable(icon);
    }
    RelativeLayout.LayoutParams imageParams =
        new RelativeLayout.LayoutParams(dpToPx(48), dpToPx(48));

    int start;
    if (orientation == Configuration.ORIENTATION_PORTRAIT) {
      start = 12;
    } else {
      start = 20;
    }

    setMargins(imageParams, start, 12, 0, 0);
    imageView.setLayoutParams(imageParams);
    return imageView;
  }

  private RelativeLayout buildMainLayout() {
    int backgroundColor = Color.parseColor("#64000000");
    RelativeLayout backgroundLayout = new RelativeLayout(activity);
    backgroundLayout.setBackgroundColor(backgroundColor);
    return backgroundLayout;
  }

  private RelativeLayout buildDialogLayout() {
    RelativeLayout dialogLayout = new RelativeLayout(activity);
    dialogLayout.setClipToPadding(false);

    GradientDrawable gradientDrawable = new GradientDrawable();
    gradientDrawable.setColor(Color.WHITE);
    gradientDrawable.setCornerRadius(dpToPx(8));
    dialogLayout.setBackground(gradientDrawable);

    int width;
    if (orientation == Configuration.ORIENTATION_PORTRAIT) {
      width = dpToPx(340);
    } else {
      width = dpToPx(544);
    }
    RelativeLayout.LayoutParams dialogLayoutParams =
        new RelativeLayout.LayoutParams(width, ViewGroup.LayoutParams.WRAP_CONTENT);
    dialogLayoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);
    dialogLayout.setLayoutParams(dialogLayoutParams);

    RelativeLayout paymentMethodsHeaderLayout = buildPaymentMethodsHeaderLayout();
    View headerSeparator = buildHeaderSeparatorLayout();
    paymentMethodsLayout = buildPaymentMethodsLayout();
    paymentMethodsLayout.setVisibility(View.INVISIBLE);
    LinearLayout buttonsView = buildButtonsView();

    dialogLayout.addView(paymentMethodsHeaderLayout);
    dialogLayout.addView(headerSeparator);
    dialogLayout.addView(paymentMethodsLayout);
    dialogLayout.addView(buttonsView);
    return dialogLayout;
  }

  private ProgressBar buildProgressBar() {
    ProgressBar progressBar = new ProgressBar(activity);
    progressBar.getIndeterminateDrawable()
        .setColorFilter(Color.parseColor("#fd786b"), PorterDuff.Mode.MULTIPLY);
    RelativeLayout.LayoutParams layoutParams =
        new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT);
    layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);
    progressBar.setLayoutParams(layoutParams);
    return progressBar;
  }

  private RelativeLayout buildPaymentMethodsLayout() {
    RelativeLayout parentLayout = new RelativeLayout(activity);
    paymentMethodsId = generateRandomId();
    parentLayout.setId(paymentMethodsId);
    RelativeLayout.LayoutParams layoutParams =
        new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT);
    layoutParams.addRule(RelativeLayout.BELOW, headerId);
    parentLayout.setLayoutParams(layoutParams);

    TextView payAsGuestView = buildPayAsGuestTextView();
    LinearLayout radioGroup = buildRadioGroupView();

    parentLayout.addView(payAsGuestView);
    parentLayout.addView(radioGroup);

    return parentLayout;
  }

  private LinearLayout buildButtonsView() {
    LinearLayout linearLayout = new LinearLayout(activity);
    RelativeLayout.LayoutParams layoutParams =
        new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT);
    layoutParams.addRule(RelativeLayout.BELOW, paymentMethodsId);

    int end, top, bottom;

    if (orientation == Configuration.ORIENTATION_PORTRAIT) {
      end = 12;
      top = 24;
      bottom = 24;
    } else {
      top = 24;
      end = 22;
      bottom = 16;
    }

    setMargins(layoutParams, 0, top, end, bottom);
    linearLayout.setGravity(Gravity.END);
    linearLayout.setOrientation(LinearLayout.HORIZONTAL);
    linearLayout.setClipChildren(false);
    linearLayout.setClipToPadding(false);
    linearLayout.setLayoutParams(layoutParams);

    cancelButton = buildCancelButtonLayout();
    positiveButton = buildPositiveButtonLayout();
    positiveButton.setEnabled(false);

    linearLayout.addView(cancelButton);
    linearLayout.addView(positiveButton);
    return linearLayout;
  }

  private Button buildPositiveButtonLayout() {
    Button button = new Button(activity);
    LinearLayout.LayoutParams layoutParams =
        new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, dpToPx(36));
    layoutParams.gravity = Gravity.CENTER_VERTICAL;
    int[] gradientColors = { Color.parseColor("#FC9D48"), Color.parseColor("#FF578C") };
    GradientDrawable enableBackground =
        new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT, gradientColors);
    enableBackground.setShape(GradientDrawable.RECTANGLE);
    enableBackground.setStroke(dpToPx(1), Color.WHITE);
    enableBackground.setCornerRadius(dpToPx(16));
    button.setTypeface(Typeface.create("sans-serif-medium", Typeface.NORMAL));
    button.setBackground(enableBackground);

    GradientDrawable disableBackground = new GradientDrawable();
    disableBackground.setShape(GradientDrawable.RECTANGLE);
    disableBackground.setStroke(dpToPx(1), Color.WHITE);
    disableBackground.setCornerRadius(dpToPx(16));
    disableBackground.setColor(Color.parseColor("#c9c9c9"));

    button.setTypeface(Typeface.create("sans-serif-medium", Typeface.NORMAL));

    StateListDrawable stateListDrawable = new StateListDrawable();
    stateListDrawable.addState(new int[] { android.R.attr.state_enabled }, enableBackground);
    stateListDrawable.addState(new int[] { -android.R.attr.state_enabled }, disableBackground);

    button.setBackground(stateListDrawable);

    button.setMaxWidth(dpToPx(142));
    button.setMinWidth(dpToPx(96));

    setPadding(button, 0, 0, 4, 0);
    button.setTextColor(Color.WHITE);
    button.setTextSize(14);
    button.setText("NEXT".toUpperCase());
    button.setLayoutParams(layoutParams);
    return button;
  }

  private Button buildCancelButtonLayout() {
    Button button = new Button(activity);
    LinearLayout.LayoutParams layoutParams =
        new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, dpToPx(36));
    layoutParams.gravity = Gravity.CENTER_VERTICAL;
    GradientDrawable background = new GradientDrawable();
    background.setShape(GradientDrawable.RECTANGLE);
    background.setStroke(dpToPx(1), Color.WHITE);
    background.setCornerRadius(dpToPx(6));
    button.setTypeface(Typeface.create("sans-serif-medium", Typeface.NORMAL));
    button.setBackground(background);
    button.setMaxWidth(dpToPx(126));
    button.setMinWidth(dpToPx(80));

    setPadding(button, 0, 0, 4, 0);
    button.setTextColor(Color.parseColor("#8a000000"));
    button.setTextSize(14);
    button.setText("Cancel".toUpperCase());
    button.setLayoutParams(layoutParams);
    return button;
  }

  private LinearLayout buildRadioGroupView() {
    LinearLayout radioGroup = new LinearLayout(activity);
    RelativeLayout.LayoutParams layoutParams =
        new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT);

    if (orientation == Configuration.ORIENTATION_PORTRAIT) {
      radioGroup.setOrientation(LinearLayout.VERTICAL);
    } else {
      radioGroup.setOrientation(LinearLayout.HORIZONTAL);
      layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);
    }

    layoutParams.addRule(RelativeLayout.BELOW, payAsGuestTextId);
    int start, end, top;
    if (orientation == Configuration.ORIENTATION_PORTRAIT) {
      start = 12;
      top = 12;
      end = 12;
    } else {
      start = 20;
      top = 18;
      end = 20;
    }
    setMargins(layoutParams, start, top, end, 0);
    radioGroup.setLayoutParams(layoutParams);

    creditCardWrapperLayout = buildCreditCardWrapperLayout();
    paypalWrapperLayout = buildPaypalWrapperLayout();
    installWrapperLayout = buildInstallWrapperLayout();

    creditCardWrapperLayout.setVisibility(View.GONE);
    paypalWrapperLayout.setVisibility(View.GONE);

    radioGroup.addView(creditCardWrapperLayout);
    radioGroup.addView(paypalWrapperLayout);
    radioGroup.addView(installWrapperLayout);

    return radioGroup;
  }

  private RelativeLayout buildInstallWrapperLayout() {
    RelativeLayout relativeLayout = new RelativeLayout(activity);

    int width, height, top, start;

    if (orientation == Configuration.ORIENTATION_PORTRAIT) {
      width = ViewGroup.LayoutParams.MATCH_PARENT;
      height = dpToPx(52);
      top = 12;
      start = 0;
    } else {
      width = dpToPx(160);
      height = dpToPx(94);
      top = 0;
      start = 12;
    }

    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(width, height);

    GradientDrawable background = new GradientDrawable();
    background.setShape(GradientDrawable.RECTANGLE);
    background.setStroke(dpToPx(1), Color.parseColor("#e3e3e3"));
    background.setCornerRadius(dpToPx(6));
    relativeLayout.setBackground(background);
    setMargins(layoutParams, start, top, 0, 0);
    relativeLayout.setLayoutParams(layoutParams);

    installRadioButtonId = generateRandomId();
    installRadioButton = buildRadioButton(installRadioButtonId);

    ImageView installCreditCardImage = buildInstallCreditCardImage();
    ImageView installPaypalImage = buildInstallPaypalImage();
    TextView installMainText = buildInstallMainText();
    installSecondaryText = buildInstallSecondaryText();

    installSecondaryText.setVisibility(View.GONE);

    relativeLayout.addView(installCreditCardImage);
    relativeLayout.addView(installPaypalImage);
    relativeLayout.addView(installMainText);
    relativeLayout.addView(installSecondaryText);
    relativeLayout.addView(installRadioButton);
    return relativeLayout;
  }

  private TextView buildInstallMainText() {
    TextView textView = new TextView(activity);
    installMainTextId = generateRandomId();
    textView.setId(installMainTextId);
    RelativeLayout.LayoutParams layoutParams =
        new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT);

    int start, top, end, textSize;

    if (orientation == Configuration.ORIENTATION_PORTRAIT) {
      start = 10;
      top = 12;
      end = 8;
      textSize = 12;
      setConstraint(layoutParams, RelativeLayout.RIGHT_OF, installPaypalId);
      setConstraint(layoutParams, RelativeLayout.LEFT_OF, installRadioButtonId);
    } else {
      top = 2;
      start = 0;
      end = 0;
      textSize = 11;
      layoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
      setConstraint(layoutParams, RelativeLayout.BELOW, installCreditCardId);
    }
    setMargins(layoutParams, start, top, end, 0);

    textView.setEllipsize(TextUtils.TruncateAt.END);
    textView.setMaxLines(1);
    textView.setTypeface(Typeface.create("sans-serif-medium", Typeface.NORMAL));
    textView.setTextColor(Color.BLACK);
    textView.setTextSize(textSize);
    textView.setText("Pay with AppCoins Wallet");
    textView.setLayoutParams(layoutParams);
    return textView;
  }

  private TextView buildInstallSecondaryText() {
    TextView textView = new TextView(activity);
    RelativeLayout.LayoutParams layoutParams =
        new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT);

    int rule;

    if (orientation == Configuration.ORIENTATION_PORTRAIT) {
      rule = RelativeLayout.CENTER_VERTICAL;
      setConstraint(layoutParams, RelativeLayout.ALIGN_START, installMainTextId);
    } else {
      rule = RelativeLayout.CENTER_HORIZONTAL;
    }

    layoutParams.addRule(rule);
    layoutParams.addRule(RelativeLayout.BELOW, installMainTextId);
    textView.setEllipsize(TextUtils.TruncateAt.END);
    textView.setMaxLines(1);
    textView.setTypeface(Typeface.create("sans-serif-medium", Typeface.NORMAL));
    textView.setTextColor(Color.parseColor("#FA6249"));
    textView.setTextSize(10);
    textView.setLayoutParams(layoutParams);
    return textView;
  }

  private ImageView buildInstallCreditCardImage() {
    ImageView imageView = new ImageView(activity);
    installCreditCardId = generateRandomId();
    imageView.setId(installCreditCardId);
    RelativeLayout.LayoutParams layoutParams =
        new RelativeLayout.LayoutParams(dpToPx(24), dpToPx(24));

    int start, top;

    if (orientation == Configuration.ORIENTATION_PORTRAIT) {
      start = 8;
      top = 0;
      layoutParams.addRule(RelativeLayout.CENTER_VERTICAL);
    } else {
      start = 58;
      top = 8;
    }

    Drawable creditCard = convertAssetDrawable(
        IMAGES_RESOURCE_PATH + "credit_card/" + "portrait/" + densityPath + "ic_credit_card.png");
    imageView.setImageDrawable(creditCard);
    setMargins(layoutParams, start, top, 0, 0);
    imageView.setLayoutParams(layoutParams);
    return imageView;
  }

  private ImageView buildInstallPaypalImage() {
    ImageView imageView = new ImageView(activity);
    installPaypalId = generateRandomId();
    imageView.setId(installPaypalId);
    RelativeLayout.LayoutParams layoutParams =
        new RelativeLayout.LayoutParams(dpToPx(20), dpToPx(20));

    int top;
    if (orientation == Configuration.ORIENTATION_PORTRAIT) {
      top = 0;
      layoutParams.addRule(RelativeLayout.CENTER_VERTICAL);
    } else {
      top = 8;
      setConstraint(layoutParams, RelativeLayout.RIGHT_OF, installCreditCardId);
    }

    Drawable paypal =
        convertAssetDrawable(IMAGES_RESOURCE_PATH + "paypal/" + densityPath + "ic_paypal.png");
    imageView.setImageDrawable(paypal);
    setConstraint(layoutParams, RelativeLayout.RIGHT_OF, installCreditCardId);
    setMargins(layoutParams, 1, top, 0, 0);
    imageView.setLayoutParams(layoutParams);
    return imageView;
  }

  private RelativeLayout buildPaypalWrapperLayout() {
    RelativeLayout relativeLayout = new RelativeLayout(activity);

    int width, height, top, start;

    if (orientation == Configuration.ORIENTATION_PORTRAIT) {
      width = ViewGroup.LayoutParams.MATCH_PARENT;
      height = dpToPx(52);
      top = 12;
      start = 0;
    } else {
      width = dpToPx(160);
      height = dpToPx(94);
      top = 0;
      start = 8;
    }

    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(width, height);

    GradientDrawable background = new GradientDrawable();
    background.setShape(GradientDrawable.RECTANGLE);
    background.setStroke(dpToPx(1), Color.parseColor("#e3e3e3"));
    background.setCornerRadius(dpToPx(6));
    relativeLayout.setBackground(background);
    setMargins(layoutParams, start, top, 0, 0);
    relativeLayout.setLayoutParams(layoutParams);

    ImageView paypalImage = buildPaypalImage();
    TextView paypalTextView = buildPaypalTextView();
    paypalRadioButton = buildRadioButton(generateRandomId());

    relativeLayout.addView(paypalImage);
    relativeLayout.addView(paypalTextView);
    relativeLayout.addView(paypalRadioButton);
    return relativeLayout;
  }

  private TextView buildPaypalTextView() {
    TextView textView = new TextView(activity);
    RelativeLayout.LayoutParams layoutParams =
        new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT);

    int start, top, rule, textSize;

    if (orientation == Configuration.ORIENTATION_PORTRAIT) {
      start = 20;
      top = 0;
      textSize = 12;
      rule = RelativeLayout.CENTER_VERTICAL;
      setConstraint(layoutParams, RelativeLayout.RIGHT_OF, paypalImageId);
    } else {
      start = 0;
      top = 16;
      textSize = 11;
      rule = RelativeLayout.CENTER_IN_PARENT;
    }

    layoutParams.addRule(rule);
    setMargins(layoutParams, start, top, 0, 0);
    textView.setEllipsize(TextUtils.TruncateAt.END);
    textView.setMaxLines(1);
    textView.setTypeface(Typeface.create("sans-serif-medium", Typeface.NORMAL));
    textView.setTextColor(Color.BLACK);
    textView.setTextSize(textSize);
    textView.setText("Using Paypal");
    textView.setLayoutParams(layoutParams);
    return textView;
  }

  private ImageView buildPaypalImage() {
    ImageView imageView = new ImageView(activity);
    paypalImageId = generateRandomId();
    imageView.setId(paypalImageId);
    RelativeLayout.LayoutParams layoutParams =
        new RelativeLayout.LayoutParams(dpToPx(25), dpToPx(25));

    int start, top, rule;
    if (orientation == Configuration.ORIENTATION_PORTRAIT) {
      start = 18;
      top = 0;
      rule = RelativeLayout.CENTER_VERTICAL;
    } else {
      start = 0;
      top = 8;
      rule = RelativeLayout.CENTER_HORIZONTAL;
    }

    layoutParams.addRule(rule);
    Drawable paypal =
        convertAssetDrawable(IMAGES_RESOURCE_PATH + "paypal/" + densityPath + "ic_paypal.png");
    imageView.setImageDrawable(paypal);
    setMargins(layoutParams, start, top, 0, 0);
    imageView.setLayoutParams(layoutParams);
    return imageView;
  }

  private RelativeLayout buildCreditCardWrapperLayout() {
    RelativeLayout relativeLayout = new RelativeLayout(activity);
    int width, height;

    if (orientation == Configuration.ORIENTATION_PORTRAIT) {
      width = ViewGroup.LayoutParams.MATCH_PARENT;
      height = dpToPx(52);
    } else {
      width = dpToPx(160);
      height = dpToPx(94);
    }

    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(width, height);

    GradientDrawable background = new GradientDrawable();
    background.setShape(GradientDrawable.RECTANGLE);
    background.setStroke(dpToPx(1), Color.parseColor("#e3e3e3"));
    background.setCornerRadius(dpToPx(6));
    relativeLayout.setBackground(background);
    relativeLayout.setLayoutParams(layoutParams);

    ImageView creditCardImage = buildCreditCardImage();
    TextView creditCardText = buildCreditCardTextView();
    creditCardRadioButton = buildRadioButton(generateRandomId());

    relativeLayout.addView(creditCardImage);
    relativeLayout.addView(creditCardText);
    relativeLayout.addView(creditCardRadioButton);
    return relativeLayout;
  }

  private RadioButton buildRadioButton(int id) {
    RadioButton radioButton = new RadioButton(activity);
    radioButton.setId(id);
    RelativeLayout.LayoutParams layoutParams =
        new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT);

    int rule, end, bottom;
    if (orientation == Configuration.ORIENTATION_PORTRAIT) {
      rule = RelativeLayout.CENTER_VERTICAL;
      end = 20;
      bottom = 0;
      setConstraint(layoutParams, RelativeLayout.ALIGN_PARENT_RIGHT);
    } else {
      rule = RelativeLayout.CENTER_HORIZONTAL;
      end = 0;
      bottom = 6;
      layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
    }

    layoutParams.addRule(rule);
    setMargins(layoutParams, 0, 0, end, bottom);
    setRadioButtonDrawable(radioButton);
    radioButton.setLayoutParams(layoutParams);
    return radioButton;
  }

  private void setRadioButtonDrawable(RadioButton radioButton) {
    StateListDrawable stateListDrawable = new StateListDrawable();
    Drawable checkedRadioButton = convertAssetDrawable(
        BUTTONS_RESOURCE_PATH + "checked/" + densityPath + "ic_radio_checked.png");
    Drawable uncheckedRadioButton = convertAssetDrawable(
        BUTTONS_RESOURCE_PATH + "unchecked/" + densityPath + "ic_radio_unchecked.png");
    stateListDrawable.addState(new int[] { android.R.attr.state_checked }, checkedRadioButton);
    stateListDrawable.addState(new int[] { -android.R.attr.state_checked }, uncheckedRadioButton);
    radioButton.setButtonDrawable(stateListDrawable);
  }

  private ImageView buildCreditCardImage() {
    ImageView imageView = new ImageView(activity);
    creditCardImageId = generateRandomId();
    imageView.setId(creditCardImageId);

    int width, height, start, rule, top;
    String path;

    if (orientation == Configuration.ORIENTATION_PORTRAIT) {
      width = 25;
      height = 25;
      start = 18;
      top = 0;
      path = "portrait/";
      rule = RelativeLayout.CENTER_VERTICAL;
    } else {
      width = 57;
      height = 10;
      start = 0;
      top = 15;
      path = "landscape/";
      rule = RelativeLayout.CENTER_HORIZONTAL;
    }

    RelativeLayout.LayoutParams layoutParams =
        new RelativeLayout.LayoutParams(dpToPx(width), dpToPx(height));
    layoutParams.addRule(rule);
    Drawable creditCard = convertAssetDrawable(
        IMAGES_RESOURCE_PATH + "credit_card/" + path + densityPath + "ic_credit_card.png");
    imageView.setImageDrawable(creditCard);
    setMargins(layoutParams, start, top, 0, 0);
    imageView.setLayoutParams(layoutParams);
    return imageView;
  }

  private TextView buildCreditCardTextView() {
    TextView textView = new TextView(activity);
    RelativeLayout.LayoutParams layoutParams =
        new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT);

    int start, top, rule, textSize;

    if (orientation == Configuration.ORIENTATION_PORTRAIT) {
      rule = RelativeLayout.CENTER_VERTICAL;
      start = 20;
      top = 0;
      textSize = 12;
      setConstraint(layoutParams, RelativeLayout.RIGHT_OF, creditCardImageId);
    } else {
      start = 0;
      top = 16;
      textSize = 11;
      rule = RelativeLayout.CENTER_IN_PARENT;
    }

    layoutParams.addRule(rule);
    setMargins(layoutParams, start, top, 0, 0);
    textView.setEllipsize(TextUtils.TruncateAt.END);
    textView.setMaxLines(1);
    textView.setTypeface(Typeface.create("sans-serif-medium", Typeface.NORMAL));
    textView.setTextColor(Color.BLACK);
    textView.setTextSize(textSize);
    textView.setText("Using Credit Card");
    textView.setLayoutParams(layoutParams);
    return textView;
  }

  private TextView buildPayAsGuestTextView() {
    TextView textView = new TextView(activity);
    payAsGuestTextId = generateRandomId();
    textView.setId(payAsGuestTextId);
    RelativeLayout.LayoutParams layoutParams =
        new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT);
    int top, start;
    if (orientation == Configuration.ORIENTATION_PORTRAIT) {
      top = 12;
      start = 12;
    } else {
      top = 16;
      start = 20;
    }
    setMargins(layoutParams, start, top, 0, 0);
    textView.setTypeface(Typeface.create("sans-serif-medium", Typeface.NORMAL));
    textView.setTextColor(Color.parseColor("#000000"));
    textView.setTextSize(14);
    textView.setText("Pay as Guest");
    textView.setLayoutParams(layoutParams);
    return textView;
  }

  private View buildHeaderSeparatorLayout() {
    View view = new View(activity);
    headerId = generateRandomId();
    view.setId(headerId);
    RelativeLayout.LayoutParams layoutParams =
        new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, dpToPx(1));
    int start, top, end;
    if (orientation == Configuration.ORIENTATION_PORTRAIT) {
      start = 16;
      top = 20;
      end = 16;
    } else {
      start = 20;
      top = 14;
      end = 20;
    }
    setMargins(layoutParams, start, top, end, 0);
    view.setBackgroundColor(Color.parseColor("#eaeaea"));
    layoutParams.addRule(RelativeLayout.BELOW, paymentMethodsHeaderId);
    view.setLayoutParams(layoutParams);
    return view;
  }

  private Drawable convertAssetDrawable(String path) {
    InputStream inputStream = null;
    try {
      inputStream = activity.getResources()
          .getAssets()
          .open(path);
    } catch (IOException e) {
      e.printStackTrace();
    }
    return Drawable.createFromStream(inputStream, null);
  }

  public void selectRadioButton(String selectedRadioButton) {
    if (selectedRadioButton != null) {
      if (selectedRadioButton.equals(PaymentMethodsFragment.CREDIT_CARD_RADIO)) {
        creditCardWrapperLayout.setBackground(getSelectedGradientDrawable());
        paypalWrapperLayout.setBackground(getDefaultGradientDrawable());
        installWrapperLayout.setBackground(getDefaultGradientDrawable());

        creditCardRadioButton.setChecked(true);
        paypalRadioButton.setChecked(false);
        installRadioButton.setChecked(false);
      } else if (selectedRadioButton.equals(PaymentMethodsFragment.PAYPAL_RADIO)) {
        paypalWrapperLayout.setBackground(getSelectedGradientDrawable());
        creditCardWrapperLayout.setBackground(getDefaultGradientDrawable());
        installWrapperLayout.setBackground(getDefaultGradientDrawable());

        creditCardRadioButton.setChecked(false);
        paypalRadioButton.setChecked(true);
        installRadioButton.setChecked(false);
      } else {
        installWrapperLayout.setBackground(getSelectedGradientDrawable());
        creditCardWrapperLayout.setBackground(getDefaultGradientDrawable());
        paypalWrapperLayout.setBackground(getDefaultGradientDrawable());

        creditCardRadioButton.setChecked(false);
        paypalRadioButton.setChecked(false);
        installRadioButton.setChecked(true);
      }
    }
  }

  private GradientDrawable getSelectedGradientDrawable() {
    if (selectedBackground == null) {
      selectedBackground = new GradientDrawable();
      selectedBackground.setShape(GradientDrawable.RECTANGLE);
      selectedBackground.setStroke(dpToPx(1), Color.parseColor("#fe6e76"));
      selectedBackground.setCornerRadius(dpToPx(6));
    }
    return selectedBackground;
  }

  private GradientDrawable getDefaultGradientDrawable() {
    if (defaultBackground == null) {
      defaultBackground = new GradientDrawable();
      defaultBackground.setShape(GradientDrawable.RECTANGLE);
      defaultBackground.setStroke(dpToPx(1), Color.parseColor("#e3e3e3"));
      defaultBackground.setCornerRadius(dpToPx(6));
    }
    return defaultBackground;
  }

  public void onDestroyView() {
    fiatPriceView = null;
    appcPriceView = null;
    creditCardRadioButton = null;
    paypalRadioButton = null;
    installRadioButton = null;
    cancelButton = null;
    positiveButton = null;
    creditCardWrapperLayout = null;
    paypalWrapperLayout = null;
    installWrapperLayout = null;
    selectedBackground = null;
    defaultBackground = null;
    paymentMethodsLayout = null;
    errorView = null;
    dialogLayout = null;
    installSecondaryText = null;
    intentLoadingView = null;
    paymentErrorViewLayout = null;
  }

  public TextView getFiatPriceView() {
    return fiatPriceView;
  }

  public TextView getAppcPriceView() {
    return appcPriceView;
  }

  public RadioButton getCreditCardRadioButton() {
    return creditCardRadioButton;
  }

  public RadioButton getPaypalRadioButton() {
    return paypalRadioButton;
  }

  public RadioButton getInstallRadioButton() {
    return installRadioButton;
  }

  public Button getCancelButton() {
    return cancelButton;
  }

  public Button getPositiveButton() {
    return positiveButton;
  }

  public ViewGroup getCreditCardWrapperLayout() {
    return creditCardWrapperLayout;
  }

  public ViewGroup getPaypalWrapperLayout() {
    return paypalWrapperLayout;
  }

  public ViewGroup getInstallWrapperLayout() {
    return installWrapperLayout;
  }

  public ViewGroup getPaymentMethodsLayout() {
    return paymentMethodsLayout;
  }

  public Button getErrorPositiveButton() {
    return paymentErrorViewLayout.getErrorPositiveButton();
  }

  public void setErrorMessage(String message) {
    paymentErrorViewLayout.setMessage(message);
  }

  public ViewGroup getErrorView() {
    return errorView;
  }

  public ViewGroup getDialogLayout() {
    return dialogLayout;
  }

  public TextView getInstallSecondaryText() {
    return installSecondaryText;
  }

  public ViewGroup getIntentLoadingView() {
    return intentLoadingView;
  }
}