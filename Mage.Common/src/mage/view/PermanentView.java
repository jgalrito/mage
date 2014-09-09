/*
* Copyright 2010 BetaSteward_at_googlemail.com. All rights reserved.
*
* Redistribution and use in source and binary forms, with or without modification, are
* permitted provided that the following conditions are met:
*
*    1. Redistributions of source code must retain the above copyright notice, this list of
*       conditions and the following disclaimer.
*
*    2. Redistributions in binary form must reproduce the above copyright notice, this list
*       of conditions and the following disclaimer in the documentation and/or other materials
*       provided with the distribution.
*
* THIS SOFTWARE IS PROVIDED BY BetaSteward_at_googlemail.com ``AS IS'' AND ANY EXPRESS OR IMPLIED
* WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
* FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL BetaSteward_at_googlemail.com OR
* CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
* CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
* SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON
* ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
* NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF
* ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
*
* The views and conclusions contained in the software and documentation are those of the
* authors and should not be interpreted as representing official policies, either expressed
* or implied, of BetaSteward_at_googlemail.com.
*/

package mage.view;

import mage.abilities.Ability;
import mage.abilities.common.TurnFaceUpAbility;
import mage.abilities.common.TurnedFaceUpTriggeredAbility;
import mage.cards.Card;
import mage.constants.Rarity;
import mage.game.Game;
import mage.game.permanent.Permanent;
import mage.game.permanent.PermanentToken;
import mage.players.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 *
 * @author BetaSteward_at_googlemail.com
 */
public class PermanentView extends CardView {
    private static final long serialVersionUID = 1L;

    private boolean tapped;
    private final boolean flipped;
    private final boolean phasedIn;
    private final boolean summoningSickness;
    private final int damage;
    private List<UUID> attachments;
    private final CardView original;
    private final boolean copy;
    private final String nameOwner; // only filled if != controller
    private final boolean controlled;
    private UUID attachedTo;

    public PermanentView(Permanent permanent, Card card, UUID createdForPlayerId, Game game) {
        super(permanent, null, permanent.getControllerId().equals(createdForPlayerId));
        this.controlled = permanent.getControllerId().equals(createdForPlayerId);
        this.rules = permanent.getRules();
        this.tapped = permanent.isTapped();
        this.flipped = permanent.isFlipped();
        this.phasedIn = permanent.isPhasedIn();
        this.summoningSickness = permanent.hasSummoningSickness();
        this.damage = permanent.getDamage();
        if (permanent.getAttachments().size() > 0) {
            attachments = new ArrayList<>();
            attachments.addAll(permanent.getAttachments());
        }
        this.attachedTo = permanent.getAttachedTo();
        if (isToken()) {
            original = new CardView(((PermanentToken)permanent).getToken());
            original.expansionSetCode = permanent.getExpansionSetCode();
            tokenSetCode = original.getTokenSetCode();
        }
        else {
            if (card != null) {
                original = new CardView(card);
            } else {
                original = null;
            }
        }
        this.transformed = permanent.isTransformed();
        this.copy = permanent.isCopy();

        // for fipped, transformed or copied cards, switch the names
        if (original != null && !original.getName().equals(this.getName())) {
            if (permanent.isCopy() && permanent.isFlipCard()) {
                this.alternateName = permanent.getFlipCardName();
                this.originalName = this.getName();
            } else {
                this.alternateName = original.getName();
                this.originalName = this.getName();
            }
        }
        if (!permanent.getOwnerId().equals(permanent.getControllerId())) {
            Player owner = game.getPlayer(permanent.getOwnerId());
            if (owner != null) {
                this.nameOwner = owner.getName();
            } else {
                this.nameOwner = "";
            }
        } else {
           this.nameOwner = ""; 
        }
        
        if (permanent.isFaceDown() && permanent.isMorphCard()) {
            // add morph rule text
            if (card != null) {
                if (controlled) {
                    for (Ability permanentAbility : permanent.getAbilities()) {
                        if (permanentAbility instanceof TurnFaceUpAbility && !permanentAbility.getRuleVisible()) {
                            this.rules.add(permanentAbility.getRule(true));
                        }
                        if (permanentAbility.getWorksFaceDown()) {
                            this.rules.add(permanentAbility.getRule());
                        }
                    }
                    this.name = card.getName();
                    this.expansionSetCode = card.getExpansionSetCode();
                    this.cardNumber = card.getCardNumber();
                } else {
                    this.rules.add("If the controller has priority, he or she may turn this permanent face up." +
                        " This is a special action; it doesnt use the stack. To do this he or she pays the morph costs," +
                        " then turns this permanent face up.");
                    this.rarity = Rarity.COMMON;
                }

            }
        }
        
    }

    public boolean isTapped() {
        return tapped;
    }

    public int getDamage() {
        return damage;
    }

    public boolean isFlipped() {
        return flipped;
    }

    public boolean isCopy() {
        return copy;
    }

    public boolean isPhasedIn() {
        return phasedIn;
    }

    public boolean hasSummoningSickness(){
        return summoningSickness;
    }

    public List<UUID> getAttachments() {
        return attachments;
    }

    public CardView getOriginal() {
        return original;
    }

    public void overrideTapped(boolean tapped) {
        this.tapped = tapped;
    }

    public String getNameOwner() {
        return nameOwner;
    }

    public boolean isControlled() {
        return controlled;
    }

    public UUID getAttachedTo() {
        return attachedTo;
    }

    public boolean isAttachedTo() {
        return attachedTo != null;
    }
}
