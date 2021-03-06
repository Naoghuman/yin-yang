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
package com.github.naoghuman.app.yy.taichi;

import com.github.naoghuman.app.yy.color.ColorMaterialDesign;
import com.github.naoghuman.app.yy.color.ColorProvider;
import com.github.naoghuman.app.yy.configuration.ConfigurationEvent;
import com.github.naoghuman.app.yy.configuration.ConfigurationPreferences;
import com.github.naoghuman.app.yy.configuration.ConfigurationTaiChi;
import com.github.naoghuman.lib.action.core.ActionHandlerFacade;
import com.github.naoghuman.lib.action.core.TransferData;
import com.github.naoghuman.lib.logger.core.LoggerFacade;
import com.github.naoghuman.lib.preferences.core.PreferencesFacade;
import java.util.Optional;
import javafx.collections.FXCollections;
import javafx.collections.ObservableMap;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.control.Labeled;
import javafx.scene.effect.DropShadow;
import javafx.scene.paint.Color;
import javafx.scene.shape.Shape;

/**
 * 
 * @author Naoghuman
 * @since  0.5.0
 */
final class DefaultTaiChiColors implements 
        ConfigurationEvent, ConfigurationPreferences, ConfigurationTaiChi,
        TaiChiColors
{
    private static final ObservableMap<Node,    String> CONTAINERS_FOR_STYLE = FXCollections.observableHashMap();
    private static final ObservableMap<Labeled, String> LABELED_FOR_STYLE    = FXCollections.observableHashMap();
    private static final ObservableMap<Labeled, String> LABELED_FOR_TEXTFILL = FXCollections.observableHashMap();
    private static final ObservableMap<Shape,   String> SHAPES_FOR_FILL      = FXCollections.observableHashMap();
    
    private static final String STYLE__BACKGROUND_COLOR_RADIUS = "-fx-background-color:%s;-fx-background-radius:5.0;"; // NOI18N
    
    public DefaultTaiChiColors() {
        
    }
    
    @Override
    public void initialize() {
        LoggerFacade.getDefault().info(this.getClass(), "DefaultTaiChiColors.initialize()"); // NOI18N
        
    }
    
    private void onActionLoadTaiChiColors() {
        LoggerFacade.getDefault().info(this.getClass(), "DefaultTaiChiColors.onActionLoadTaiChiColors()"); // NOI18N
        
        // TODO better ColorMaterialDesign instead r,g,b String
        String yangColor = PreferencesFacade.getDefault().get(PREF__TAICHI_SYMBOL__YANG_COLOR, PREF__TAICHI_SYMBOL__YANG_COLOR_DEFAULT_VALUE);
        yangColor        = ColorMaterialDesign.get(yangColor, ColorMaterialDesign.GREY_050).rgb();
        this.onActionUpdateApplicationInfoColor(ON_ACTION__UPDATE__YANG_COLOR, yangColor);
        this.onActionUpdateApplicationMenuColor(ON_ACTION__UPDATE__YANG_COLOR, yangColor);
        this.onActionUpdateTaiChiSymbolColor(   ON_ACTION__UPDATE__YANG_COLOR, yangColor);
        this.onActionUpdateTaiChiTermsColor(    ON_ACTION__UPDATE__YANG_COLOR, yangColor);

        String yinColor = PreferencesFacade.getDefault().get(PREF__TAICHI_SYMBOL__YIN_COLOR,  PREF__TAICHI_SYMBOL__YIN_COLOR_DEFAULT_VALUE);
        yinColor        = ColorMaterialDesign.get(yinColor, ColorMaterialDesign.GREY_900).rgb();
        this.onActionUpdateApplicationInfoColor(ON_ACTION__UPDATE__YIN_COLOR, yinColor);
        this.onActionUpdateApplicationMenuColor(ON_ACTION__UPDATE__YIN_COLOR, yinColor);
        this.onActionUpdateTaiChiSymbolColor(   ON_ACTION__UPDATE__YIN_COLOR, yinColor);
        this.onActionUpdateTaiChiTermsColor(    ON_ACTION__UPDATE__YIN_COLOR, yinColor);
    }
    
    private void onActionSaveColor(final String actionId, final String color) {
        LoggerFacade.getDefault().info(this.getClass(), "DefaultTaiChiColors.onActionSaveColor(String, String)"); // NOI18N
        
        PreferencesFacade.getDefault().put(actionId, ColorMaterialDesign.get(Color.web(String.format(PATTERN__RGB_COLOR, color))).name());
    }
    
    private void onActionUpdateApplicationInfoColor(final String actionId, final String color) {
        LoggerFacade.getDefault().info(this.getClass(), "DefaultTaiChiColors.onActionUpdateApplicationInfoColor(String, String)"); // NOI18N

        CONTAINERS_FOR_STYLE.keySet().stream()
                .filter((node) -> 
                        CONTAINERS_FOR_STYLE.get(node).equals(actionId)
                )
                .forEachOrdered((node) -> {
                    node.setStyle(String.format(STYLE__BACKGROUND_COLOR_RADIUS,
                        ColorProvider.getDefault().getColorConverter().convertToBrighter(color, 0.8d)));
                });
        
        LABELED_FOR_TEXTFILL.keySet().stream()
                .filter((labeled) -> 
                        LABELED_FOR_TEXTFILL.get(labeled).equals(actionId)
                )
                .forEachOrdered((labeled) -> {
                    labeled.setTextFill(Color.web(String.format(PATTERN__RGB_COLOR, color)));
                });
    }
    
    private void onActionUpdateApplicationMenuColor(final String actionId, final String color) {
        LoggerFacade.getDefault().info(this.getClass(), "DefaultTaiChiColors.onActionUpdateApplicationMenuColor(String, String)"); // NOI18N
        
        CONTAINERS_FOR_STYLE.keySet().stream()
                .filter((node) -> 
                        CONTAINERS_FOR_STYLE.get(node).equals(actionId)
                )
                .forEachOrdered((node) -> {
                    node.setStyle(String.format(STYLE__BACKGROUND_COLOR_RADIUS,
                        ColorProvider.getDefault().getColorConverter().convertToBrighter(color, 0.8d)));
                });
    }
    
    private void onActionUpdateTaiChiSymbolColor(final String actionId, final String color) {
        LoggerFacade.getDefault().info(this.getClass(), "DefaultTaiChiColors.onActionUpdateTaiChiSymbolColor(String, String)"); // NOI18N
        
        SHAPES_FOR_FILL.keySet().stream()
                .filter((shape) ->
                        (SHAPES_FOR_FILL.get(shape).equals(actionId))
                )
                .forEachOrdered((shape) -> {
                    shape.setFill(Color.web(String.format(PATTERN__RGB_COLOR, color)));
                    if (
                            actionId.equals(ON_ACTION__UPDATE__YIN_COLOR)
                            && (shape.getEffect() instanceof DropShadow)
                    ) {
                        final DropShadow ds = (DropShadow) shape.getEffect();
                        ds.setColor(Color.web(String.format(PATTERN__RGB_COLOR, color)));
                    }
                });
    }
    
    private void onActionUpdateTaiChiTermsColor(final String actionId, final String color) {
        LoggerFacade.getDefault().info(this.getClass(), "DefaultTaiChiColors.onActionUpdateTaiChiTermsColor(String, String)"); // NOI18N

        LABELED_FOR_STYLE.keySet().stream()
                .filter((labeled) -> 
                        LABELED_FOR_STYLE.get(labeled).equals(actionId)
                )
                .forEachOrdered((labeled) -> {
                    labeled.setStyle(String.format(STYLE__BACKGROUND_COLOR_RADIUS,
                        ColorProvider.getDefault().getColorConverter().convertToBrighter(color, 0.8d)));
                });
        
        LABELED_FOR_TEXTFILL.keySet().stream()
                .filter((labeled) -> 
                        LABELED_FOR_TEXTFILL.get(labeled).equals(actionId)
                )
                .forEachOrdered((labeled) -> {
                    labeled.setTextFill(Color.web(String.format(PATTERN__RGB_COLOR, color)));
                });
    }
    
    private void onActionUpdateYangColor(final String color) {
        LoggerFacade.getDefault().info(this.getClass(), "DefaultTaiChiColors.onActionUpdateYangColor(String)"); // NOI18N

        this.onActionSaveColor(PREF__TAICHI_SYMBOL__YANG_COLOR, color);
        
        this.onActionUpdateApplicationInfoColor(ON_ACTION__UPDATE__YANG_COLOR, color);
        this.onActionUpdateApplicationMenuColor(ON_ACTION__UPDATE__YANG_COLOR, color);
        this.onActionUpdateTaiChiSymbolColor(   ON_ACTION__UPDATE__YANG_COLOR, color);
        this.onActionUpdateTaiChiTermsColor(    ON_ACTION__UPDATE__YANG_COLOR, color);
    }
    
    private void onActionUpdateYinColor(final String color) {
        LoggerFacade.getDefault().info(this.getClass(), "DefaultTaiChiColors.onActionUpdateYinColor(String)"); // NOI18N

        this.onActionSaveColor(PREF__TAICHI_SYMBOL__YIN_COLOR, color);
        
        this.onActionUpdateApplicationInfoColor(ON_ACTION__UPDATE__YIN_COLOR, color);
        this.onActionUpdateApplicationMenuColor(ON_ACTION__UPDATE__YIN_COLOR, color);
        this.onActionUpdateTaiChiSymbolColor(   ON_ACTION__UPDATE__YIN_COLOR, color);
        this.onActionUpdateTaiChiTermsColor(    ON_ACTION__UPDATE__YIN_COLOR, color);
    }
    
    @Override
    public void register(final Labeled labeled, final TaiChiColorType type, final String actionId) {
        LoggerFacade.getDefault().info(this.getClass(), "DefaultTaiChiColors.register(Labeled, TaiChiColorType, String)"); // NOI18N
        
        switch(type) {
            case STYLE: {
                if(!LABELED_FOR_STYLE.containsKey(labeled)) {
                    LABELED_FOR_STYLE.put(labeled, actionId);
                }
                break;
            }
            case TEXTFILL: {
                if(!LABELED_FOR_TEXTFILL.containsKey(labeled)) {
                    LABELED_FOR_TEXTFILL.put(labeled, actionId);
                }
                break;
            }
        }
    }
    
    @Override
    public void register(final Node node, final String actionId) {
        LoggerFacade.getDefault().info(this.getClass(), "DefaultTaiChiColors.register(Node, String)"); // NOI18N
        
        if (!CONTAINERS_FOR_STYLE.containsKey(node)) {
            CONTAINERS_FOR_STYLE.put(node, actionId);
        }
    }
    
    @Override
    public void register(final Shape shape, final String actionId) {
        LoggerFacade.getDefault().info(this.getClass(), "DefaultTaiChiColors.register(Shape, String)"); // NOI18N
        
        if (!SHAPES_FOR_FILL.containsKey(shape)) {
            SHAPES_FOR_FILL.put(shape, actionId);
        }
    }

    @Override
    public void register() {
        LoggerFacade.getDefault().info(this.getClass(), "DefaultTaiChiColors.register()"); // NOI18N
        
        this.registerOnActionLoadTaiChiColors();
        this.registerOnActionUpdateYangColor();
        this.registerOnActionUpdateYinColor();
    }
    
    private void registerOnActionLoadTaiChiColors() {
        LoggerFacade.getDefault().info(this.getClass(), "DefaultTaiChiColors.registerOnActionLoadTaiChiColors()"); // NOI18N
        
        ActionHandlerFacade.getDefault().register(
                ON_ACTION__LOAD__TAI_CHI_COLORS,
                (ActionEvent event) -> {
                    this.onActionLoadTaiChiColors();
                });
    }

    private void registerOnActionUpdateYangColor() {
        LoggerFacade.getDefault().info(this.getClass(), "DefaultTaiChiColors.registerOnActionUpdateYangColor()"); // NOI18N
        
        ActionHandlerFacade.getDefault().register(
                ON_ACTION__UPDATE__YANG_COLOR,
                (ActionEvent event) -> {
                    final Object source = event.getSource();
                    if (source instanceof TransferData) {
                        final TransferData     transferData = (TransferData) source;
                        final Optional<String> optional     = transferData.getString();
                        if(optional.isPresent()) {
                            final String color = optional.get();
                            this.onActionUpdateYangColor(color);
                        }
                    }
                });
    }

    private void registerOnActionUpdateYinColor() {
        LoggerFacade.getDefault().info(this.getClass(), "DefaultTaiChiColors.registerOnActionUpdateYinColor()"); // NOI18N
        
        ActionHandlerFacade.getDefault().register(
                ON_ACTION__UPDATE__YIN_COLOR,
                (ActionEvent event) -> {
                    final Object source = event.getSource();
                    if (source instanceof TransferData) {
                        final TransferData     transferData = (TransferData) source;
                        final Optional<String> optional     = transferData.getString();
                        if(optional.isPresent()) {
                            final String color = optional.get();
                            this.onActionUpdateYinColor(color);
                        }
                    }
                });
    }
    
}
