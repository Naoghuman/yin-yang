/*
 * Copyright (C) 2018 Naoghuman
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.github.naoghuman.yin.yang.taichi;

import com.github.naoghuman.lib.action.core.ActionHandlerFacade;
import com.github.naoghuman.lib.action.core.RegisterActions;
import com.github.naoghuman.lib.logger.core.LoggerFacade;
import com.github.naoghuman.lib.preferences.core.PreferencesFacade;
import com.github.naoghuman.yin.yang.color.ColorConverter;
import com.github.naoghuman.yin.yang.configuration.EventConfiguration;
import com.github.naoghuman.yin.yang.configuration.I18nConfiguration;
import com.github.naoghuman.yin.yang.configuration.PreferencesConfiguration;
import com.github.naoghuman.yin.yang.i18n.I18nProvider;
import java.util.Optional;
import java.util.Random;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.PauseTransition;
import javafx.animation.SequentialTransition;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.util.Duration;
import com.github.naoghuman.yin.yang.configuration.TaiChiConfiguration;

/**
 *
 * @author Naoghuman
 * @since  0.4.0
 */
public class TaiChiTerms implements 
        EventConfiguration, I18nConfiguration, PreferencesConfiguration,
        RegisterActions, TaiChiConfiguration
{
    private static final char   TERM_SEPERATOR = ';'; // NOI18N
    private static final double OPACITY__TERM  = 1.0d;
    private static final double OPACITY__ZERO  = 0.0d;
    
    private static final Random RANDOM                              = new Random();
    private static final String PATTERN__RGB_COLOR                  = "rgb(%s)"; // NOI18N
    private static final String STYLE_TERM__BACKGROUND_COLOR_RADIUS = "-fx-background-color:%s;-fx-background-radius:5.0;"; // NOI18N
    
    private static final Optional<TaiChiTerms> INSTANCE = Optional.of(new TaiChiTerms());

    public static final TaiChiTerms getDefault() {
        return INSTANCE.get();
    }
    
    private double diameterTheOne  = PREF__TAICHI_SYMBOL__DIAMETER_DEFAULT_VALUE;
    private int    termIndex       = 0;
    private int    termMaxQuantity = 0;
    
    private HBox  hbTaiChiTerms;
    private Label lYangTerm;
    private Label lYinTerm;
    
    private TaiChiTerms() {
        this.initialize();
    }
    
    private void initialize() {
        LoggerFacade.getDefault().info(this.getClass(), "TaiChiTerms.initialize()"); // NOI18N
        
        diameterTheOne  = PreferencesFacade.getDefault().getDouble(PREF__TAICHI_SYMBOL__DIAMETER, PREF__TAICHI_SYMBOL__DIAMETER_DEFAULT_VALUE);
        termMaxQuantity = Integer.parseInt(I18nProvider.getDefault().getI18nYinYang().getProperty(I18N_KEY__TAICHI__TERM_QUANTITY));
        
        this.register();
    }
    
    public void configure(final HBox hbTaiChiTerms, final Label lYinTerm, final Label lYangTerm) {
        LoggerFacade.getDefault().debug(this.getClass(), "TaiChiTerms.configure(HBox, Label, Label)"); // NOI18N
        
        this.hbTaiChiTerms = hbTaiChiTerms;
        this.lYinTerm      = lYinTerm;
        this.lYangTerm     = lYangTerm;
        
        diameterTheOne = PreferencesFacade.getDefault().getDouble(PREF__TAICHI_SYMBOL__DIAMETER, PREF__TAICHI_SYMBOL__DIAMETER_DEFAULT_VALUE);
        
        this.hbTaiChiTerms.getChildren().clear();
//        this.hbYinYangTerms.setPrefHeight(PREF__YIN_YANG__SYMBOL_DIAMETER_DEFAULT_VALUE / 8.0d / 2.0d + YIN_YANG_SYMBOLE__OUTER_BORDER * 2.0d);
        this.hbTaiChiTerms.setPrefWidth(diameterTheOne - diameterTheOne / 4.0d);
        this.hbTaiChiTerms.setLayoutX(diameterTheOne / 8.0d + TAICHI_SYMBOL__OUTER_BORDER);
//        this.hbYinYangTerms.setLayoutY((diameterTheOne / 2.0d + YIN_YANG_SYMBOLE__OUTER_BORDER));
//        this.hbYinYangTerms.setStyle("-fx-background-color:red");
        
        this.lYinTerm.setAlignment(Pos.CENTER);
        this.lYinTerm.setOpacity(OPACITY__ZERO);
        this.lYinTerm.setPrefWidth(diameterTheOne / 2.0d);
        
        this.lYangTerm.setAlignment(Pos.CENTER);
        this.lYangTerm.setOpacity(OPACITY__ZERO);
        this.lYangTerm.setPrefWidth(diameterTheOne / 2.0d);
        
        this.onActionUpdateTermColors();
    }
    
    /**
     * 1) Start delay
     * 2) Blend in   YinTerm
     * 3) Delay show YinTerm
     * 4) Blend out  YinTerm
     * 5) Switch to  YangTerm
     * 6) Blend in   YangTerm
     * 7) Delay show YangTerm
     * 8) Blend out  YangTerm
     * 9) Like 1)
     */
    private SequentialTransition createSequentialTransition() {
        // Comment out to avoid spawning messages
//        LoggerFacade.getDefault().debug(this.getClass(), "YinYangTerms.createSequentialTransition()"); // NOI18N
        
        final SequentialTransition st = new SequentialTransition();
        termIndex = RANDOM.nextInt(termMaxQuantity) + 1;

        // 1
        PauseTransition pt = new PauseTransition();
        pt.setDuration(Duration.millis(5000.0d + RANDOM.nextInt(15000)));
        pt.setOnFinished((event) -> {
            hbTaiChiTerms.getChildren().clear();
            hbTaiChiTerms.setAlignment(Pos.CENTER_RIGHT);
            hbTaiChiTerms.getChildren().add(lYinTerm);
            
            final String termYinYang = I18nProvider.getDefault().getI18nYinYang().getProperty(String.format(I18N_KEY__TAICHI__TERM_NR, termIndex));
            final String termYin     = this.extractYinTerm(termYinYang);
            lYinTerm.setText(termYin);
        });
        st.getChildren().add(pt);
        
        // 2
        Timeline tl = new Timeline(
                new KeyFrame(Duration.ZERO,           new KeyValue(lYinTerm.opacityProperty(), OPACITY__ZERO)),
                new KeyFrame(Duration.millis(500.0d), new KeyValue(lYinTerm.opacityProperty(), OPACITY__TERM)));
        st.getChildren().add(tl);
        
        // 3
        final double durationShowTerm = 5000.0d + RANDOM.nextInt(15000);
        pt = new PauseTransition();
        pt.setDuration(Duration.millis(durationShowTerm));
        st.getChildren().add(pt);
        
        // 4
        tl = new Timeline(
                new KeyFrame(Duration.ZERO,           new KeyValue(lYinTerm.opacityProperty(), OPACITY__TERM)),
                new KeyFrame(Duration.millis(500.0d), new KeyValue(lYinTerm.opacityProperty(), OPACITY__ZERO)));
        st.getChildren().add(tl);
        
        // 5
        pt = new PauseTransition();
        pt.setDuration(Duration.millis(750.0d));
        pt.setOnFinished((event) -> {
            hbTaiChiTerms.getChildren().clear();
            hbTaiChiTerms.setAlignment(Pos.CENTER_LEFT);
            hbTaiChiTerms.getChildren().add(lYangTerm);
            
            final String termYinYang = I18nProvider.getDefault().getI18nYinYang().getProperty(String.format(I18N_KEY__TAICHI__TERM_NR, termIndex));
            final String termYang    = this.extractYangTerm(termYinYang);
            lYangTerm.setText(termYang);
        });
        st.getChildren().add(pt);
        
        // 6
        tl = new Timeline(
                new KeyFrame(Duration.ZERO,           new KeyValue(lYangTerm.opacityProperty(), OPACITY__ZERO)),
                new KeyFrame(Duration.millis(500.0d), new KeyValue(lYangTerm.opacityProperty(), OPACITY__TERM)));
        st.getChildren().add(tl);
        
        // 7
        pt = new PauseTransition();
        pt.setDuration(Duration.millis(durationShowTerm));
        st.getChildren().add(pt);
        
        // 8
        tl = new Timeline(
                new KeyFrame(Duration.ZERO,           new KeyValue(lYangTerm.opacityProperty(), OPACITY__TERM)),
                new KeyFrame(Duration.millis(500.0d), new KeyValue(lYangTerm.opacityProperty(), OPACITY__ZERO)));
        st.getChildren().add(tl);
        
        return st;
    }
    
    private String extractYangTerm(final String termYinYang) {
        return termYinYang.substring(termYinYang.indexOf(TERM_SEPERATOR) + 1, termYinYang.length());
    }
    
    private String extractYinTerm(final String termYinYang) {
        return termYinYang.substring(0, termYinYang.indexOf(TERM_SEPERATOR));
    }

    public void onActionShowTaiChiTerms() {
        // Comment out to avoid spawning messages
//        LoggerFacade.getDefault().debug(this.getClass(), "YinYangTerms.onActionShowYinAndYangTerm()"); // NOI18N
        
        final SequentialTransition st = this.createSequentialTransition();
        st.setOnFinished((event) -> {
            this.onActionShowTaiChiTerms();
        });
        
        st.playFromStart();
    }
    
    private void onActionUpdateLanguageTaiChiTerms() {
        LoggerFacade.getDefault().debug(this.getClass(), "TaiChiTerms.onActionUpdateLanguageTaiChiTerms()"); // NOI18N
        
        final String termYinYang = I18nProvider.getDefault().getI18nYinYang().getProperty(String.format(I18N_KEY__TAICHI__TERM_NR, termIndex));
        final String termYin     = this.extractYinTerm(termYinYang);
        lYinTerm.setText(termYin);
        
        final String termYang = this.extractYangTerm(termYinYang);
        lYangTerm.setText(termYang);
    }
    
    private void onActionUpdateTermColors() {
        LoggerFacade.getDefault().debug(this.getClass(), "TaiChiTerms.onActionUpdateTermColors()"); // NOI18N
        
        final String yinSelectedColor  = PreferencesFacade.getDefault().get(PREF__TAICHI_SYMBOL__YIN_COLOR,  PREF__TAICHI_SYMBOL__YIN_COLOR_DEFAULT_VALUE);
        final String yangSelectedColor = PreferencesFacade.getDefault().get(PREF__TAICHI_SYMBOL__YANG_COLOR, PREF__TAICHI_SYMBOL__YANG_COLOR_DEFAULT_VALUE);
        
        this.lYinTerm.setStyle(String.format(
                STYLE_TERM__BACKGROUND_COLOR_RADIUS,
                ColorConverter.convertToBrighter(yinSelectedColor, 0.8d)));
        this.lYinTerm.setTextFill(Color.web(String.format(PATTERN__RGB_COLOR, yangSelectedColor)));
        
        this.lYangTerm.setStyle(String.format(
                STYLE_TERM__BACKGROUND_COLOR_RADIUS,
                ColorConverter.convertToBrighter(yangSelectedColor, 0.8d)));
        this.lYangTerm.setTextFill(Color.web(String.format(PATTERN__RGB_COLOR, yinSelectedColor)));
    }

    @Override
    public void register() {
        LoggerFacade.getDefault().debug(this.getClass(), "TaiChiTerms.register()"); // NOI18N
        
        this.registerOnActionUpdateLanguageTaiChiTerms();
        this.registerOnActionUpdateTermColors();
    }
    
    private void registerOnActionUpdateLanguageTaiChiTerms() {
        LoggerFacade.getDefault().info(this.getClass(), "TaiChiTerms.registerOnActionUpdateLanguageTaiChiTerms()"); // NOI18N
        
        ActionHandlerFacade.getDefault().register(
                ON_ACTION__UPDATE__LANGUAGE_IN_TAICHI_TERMS,
                (ActionEvent event) -> {
                    this.onActionUpdateLanguageTaiChiTerms();
                });
    }
    
    private void registerOnActionUpdateTermColors() {
        LoggerFacade.getDefault().info(this.getClass(), "TaiChiTerms.registerOnActionUpdateTermColors()"); // NOI18N
        
        ActionHandlerFacade.getDefault().register(
                ON_ACTION__UPDATE_TERM_COLORS,
                (ActionEvent event) -> {
                    this.onActionUpdateTermColors();
                });
    }
    
    
}