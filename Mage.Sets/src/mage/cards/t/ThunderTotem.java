/*
 *  Copyright 2010 BetaSteward_at_googlemail.com. All rights reserved.
 *
 *  Redistribution and use in source and binary forms, with or without modification, are
 *  permitted provided that the following conditions are met:
 *
 *     1. Redistributions of source code must retain the above copyright notice, this list of
 *        conditions and the following disclaimer.
 *
 *     2. Redistributions in binary form must reproduce the above copyright notice, this list
 *        of conditions and the following disclaimer in the documentation and/or other materials
 *        provided with the distribution.
 *
 *  THIS SOFTWARE IS PROVIDED BY BetaSteward_at_googlemail.com ``AS IS'' AND ANY EXPRESS OR IMPLIED
 *  WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 *  FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL BetaSteward_at_googlemail.com OR
 *  CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 *  CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 *  SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON
 *  ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 *  NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF
 *  ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 *  The views and conclusions contained in the software and documentation are those of the
 *  authors and should not be interpreted as representing official policies, either expressed
 *  or implied, of BetaSteward_at_googlemail.com.
 */
package mage.cards.t;

import java.util.UUID;
import mage.MageInt;
import mage.abilities.common.SimpleActivatedAbility;
import mage.abilities.costs.mana.ManaCostsImpl;
import mage.abilities.effects.common.continuous.BecomesCreatureSourceEffect;
import mage.abilities.keyword.FirstStrikeAbility;
import mage.abilities.keyword.FlyingAbility;
import mage.abilities.mana.WhiteManaAbility;
import mage.cards.CardImpl;
import mage.cards.CardSetInfo;
import mage.constants.CardType;
import mage.constants.SubType;
import mage.constants.Duration;
import mage.constants.Zone;
import mage.game.permanent.token.TokenImpl;
import mage.game.permanent.token.Token;

/**
 *
 * @author fireshoes
 */
public class ThunderTotem extends CardImpl {

    public ThunderTotem(UUID ownerId, CardSetInfo setInfo) {
        super(ownerId,setInfo,new CardType[]{CardType.ARTIFACT},"{3}");

        // {tap}: Add {W}.
        this.addAbility(new WhiteManaAbility());
        
        // {1}{W}{W}: Thunder Totem becomes a 2/2 white Spirit artifact creature with flying and first strike until end of turn.
        this.addAbility(new SimpleActivatedAbility(
                Zone.BATTLEFIELD, 
                new BecomesCreatureSourceEffect(new ThunderTotemToken(), "", Duration.EndOfTurn),
                new ManaCostsImpl("{1}{W}{W}")));
    }

    public ThunderTotem(final ThunderTotem card) {
        super(card);
    }

    @Override
    public ThunderTotem copy() {
        return new ThunderTotem(this);
    }

    private static class ThunderTotemToken extends TokenImpl {
        ThunderTotemToken() {
            super("", "2/2 white Spirit artifact creature with flying and first strike");
            cardType.add(CardType.ARTIFACT);
            cardType.add(CardType.CREATURE);
            color.setWhite(true);
            this.subtype.add(SubType.SPIRIT);
            power = new MageInt(2);
            toughness = new MageInt(2);
            this.addAbility(FlyingAbility.getInstance());
            this.addAbility(FirstStrikeAbility.getInstance());
        }
        public ThunderTotemToken(final ThunderTotemToken token) {
            super(token);
        }
    
        public ThunderTotemToken copy() {
            return new ThunderTotemToken(this);
        }
    }
}