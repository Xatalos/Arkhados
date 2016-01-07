---
title: How to play
layout: page
---

How to get into game
=====================

## Joining server ##

1. Open Arkhados client.
2. Choose your video settings.
3. (Optional) Configure your input and camera settings.
4. Press "Join Game" in the main menu.
5. Enter address and port of the server you want to join and your desired nick name.
6. Press "Start Game" and choose your hero.
7. Have fun! :)

**Note**: After each game it's recommended that you restart the client, just to be sure.

## Hosting server ##

1. Configure your server by editing server settings file.
2. Make sure that your configured port (or default port 12345) is open and forwarded to your computer.
3. Start Arkhados server.
4. Join your server using the Arkhados client. Use "localhost" (without quotes) as address.
5. Get your external IP-address. You can get it from many places, such as [whatismyip.com](https://www.whatismyip.com/).
6. Give your address and port to other players.
7. Enjoy!
8. If the game ends successfully, its replay is saved to the replays-directory. Share that replay with other players.

If others can't join your server, make sure that Arkhados isn't blocked by a firewall. If you are behind NAT, you probably need to forward some port to your IP. Visit [portforward.com](http://portforward.com/) for step-by-step port forwarding guides.

Game Modes
==========

## Deathmatch ##
In Deathmatch the first player to get certain amount of kills wins. Everyone is your enemy. If you die, you can pick new hero and then wait for respawn.

## Team Deathmatch ##
Like Deathmatch but players are grouped into teams. The first team to get certain amount of kills wins. Respawning works like in Deathmatch but you have to wait bit longer (unless configured otherwise).

Hero Guides
============

## General information ##

### Spell set ###

While every hero is unique, usually their spell sets are divided in the same way.

- **M1:** Main offensive spell. Extremely low cooldown. Bound to left mouse button by default.
- **M2:** Typically ranged spell, even for melee heroes. Bound to right mouse button by default.
- **Q:** Typically utility spell. Often used for *crowd control*. Bound to Q by default.
- **E:** Usually hero's most powerful offensive spell. Bound to E by default.
- **R:** Protective and buff spell. Bound to R by default.
- **Space:** Movement spell. Bound to Space by default.
- **Passive:** Each hero has passive "spell" that they can't activate anytime they want.

Certain spells are divided into **primary** and **secondary spells**. Primary and secondary spells of same spell share same cooldown and are quite similar. Each hero has two secondary spells.

### Buffs and debuffs ###

**Note** that these aren't the only buffs and debuffs. There are also variations of each of these buffs and debuffs.

#### Positive buffs ####
- **Speed:** Player gets higher movement speed.
- **Armor:** Player takes less damage from any attack. Deflected damage is removed from armor amount.
- **Lifesteal:** Whenever player does damage, they are healed for a certain amount based on damage done.
- **Damage boost:** Player's offensive spells do more damage.
- **Heal over time:** Player's health is restored over time.

#### Common crowd control debuffs ####

- **Stun:** Player can't move or cast spells (there are currently no stuns in game).
- **Incapacitate:** Like stun, but if player under incapacitate takes damage,  incapacitate is dispelled.
- **Silence:** Player can't cast spells but can move.
- **Petrify:** Like stun, but player takes heavily reduced damage. Has damage cap.
- <img class="spell-icon" src="Images/BuffIcons/skull.png" />**Fear:** Player starts running away from the source of fear. Player can't cast or control their movement during fear. Fear is dispelled on damage (there are exceptions).
- <img class="spell-icon" src="Images/BuffIcons/Blind.png" /> **Blind:** Player under blind has very short sight range and thus can't see others unless they are very close.
- <img class="spell-icon" src="Images/BuffIcons/Slow.png" /> **Slow:** Player's movement is slower.
- **Numb:** Player's cast speed is slower.
- **Weaken:** Player's offensive spells do less damage.

#### Other typical debuffs ####
- **Damage over time:** Player takes damage over time.


### Resting ###
If you stand still, don't use any of your abilities and don't take damage, you will start to regenerate after short period of time. Resting gets stronger and stronger as you stand still.

### Healing cap ###
In Arkhados, every character (unless specified otherwise) has a healing cap. Healing cap prevents characters from healing themselves to high hitpoints if their hitpoints get low at some points.

Healing cap is calculated with the following formula: cap = record\_low\_hp + constant .

For example, suppose Rock Golem's hitpoints drop from full to 1000 and constant is 400. That means that Rock Golem's hitpoints can never be restored to over 1400 through healing.

There is one exception to this rule. *Resting* raises record\_low\_hp exactly as much as it heals you. So if Rock Golem through resting regenerates 200 hitpoints, its healing cap is promoted to 1600.

### General tips ###

- Be especially careful how you use your Space-spell. No other spell gives you as much survivability as Space.
  - With ranged heroes Space is often better used as escape mechanism.
  - Melee and tank heroes however often need Space to get close to their targets or to initiate.
- Though M1 is usually your weakest offensive skill, it also has lowest cooldown.
- Learn to predict your opponents' actions. Often failed prediction is better than not predicting at all.
- Only fight battles where you have the advantage! Even battles lead to loss of large amounts of health and your enemy might escape just before the killing blow. Someone will "steal your kill" and you are left weak. Even if you get the kill, you will be next one to die.
- Don't stand in the fire!

## Ember Mage ##

**Lore**

The Ember Mage, Atriosis, has researched fire magic for hundreds of years
and even sacrificed his own soul to gain greater power. Now he’s but a husk
of his human self, driven only by his desire to create more powerful spells
of fire and destruction. He views his enemies as mere test subjects for
improving his magic. Anyone facing him should prepare themselves for a death
akin to torture.

**Description**

Ember Mage focuses on destructive long-range spells that cause his enemies to burn for damage over time.
Since he is weak in close range, it's better to keep a fair distance to other heroes.

### Skills ###

#### <img class="spell-icon" src="Images/SpellIcons/fireball.png" />M1: Fireball ####
Shoots a medium-long-range projectile in the target direction. Short cooldown. Adds **Brimstone** stack on hit. Same character may have at most three Brimstone stacks.

#### <img class="spell-icon" src="Images/SpellIcons/magma_bash.png" /> M2: Magma Bash (primary) ####
Shoots a long-range projectile with a *incapacitate* effect in the target direction. Medium cooldown.

Length of incapacitate is higher if target has more **Brimstone** stacks. All Brimstone stacks are consumed on hit.

#### <img class="spell-icon" src="Images/SpellIcons/magma_bash.png" /> M2: Magma Release (secondary) ####
Like Magma Bash but instead of incapacitating, it does more damage.

The damage is higher if target has more **Brimstone** stacks. All Brimstone stacks are consumed on hit.

#### <img class="spell-icon" src="Images/SpellIcons/ember_circle.png" />Q: Ember Circle ####
Creates a burning area in the target location. There is brief delay before burning activates. Once Ember circle is activated, it damages and *slows* enemies on target area. Medium cooldown.

#### <img class="spell-icon" src="Images/SpellIcons/meteor.png" />E: Meteor ####
Drops a huge meteor in the target location. The closer the meteor hits the higher the damage and knockback effect. Long cooldown.

#### <img class="spell-icon" src="Images/SpellIcons/purifying_flame.png" />R: Purifying Flame (primary) ####
Creates a shield that deflects any projectile-based attacks and burns nearby enemies. Long cooldown.

#### <img class="spell-icon" src="Images/SpellIcons/purifying_flame.png" />R: Ethereal Flame (secondary) ####
Instead of deflecting projectiles, Ethereal Flame puts Ember Mage in **trance** state. If Ember Mage takes direct* attack during trance, it is completely deflected and Ember Mage teleports to the target location.

#### <img class="spell-icon" src="Images/SpellIcons/flame_walk.png" />Space: Firewalk ####
Become invulnerable and unstoppable while quickly moving to the target location and burning enemies in your path. Long cooldown.

#### <img class="spell-icon" src="Images/BuffIcons/ignite.png" />Passive: Ignite ####
Ignite is small **Damage over time** debuff that is applied through any of Ember Mage's spells except for Ethereal Flame. Ignite has short cooldown.


### Tips ###
- Ember Mage is fragile and has mediocre movement speed. Try to keep your distance.
- R-primary <img class="spell-icon" src="Images/SpellIcons/purifying_flame.png" /> is your best defense against ranged characters but less useful against melee heroes.
- When escaping from melee heroes and you have option to choose between R <img class="spell-icon" src="Images/SpellIcons/purifying_flame.png" /> and Space <img class="spell-icon" src="Images/SpellIcons/flame_walk.png" />, you might want to consider using R-secondary first since Space is more flexible.
- Try to land your M2 <img class="spell-icon" src="Images/SpellIcons/magma_bash.png" /> on enemy **after** you have hit them with couple *Fireballs first*<img class="spell-icon" src="Images/SpellIcons/fireball.png" />. *Brimstone*-stacks amplify M2's effect.
- On the other hand, if melee hero reaches you before you've stacked Brimstone on them, don't waste time with Fireballs.
- If you hit enemy with *Magma Bash*<img class="spell-icon" src="Images/SpellIcons/magma_bash.png" />, follow it up with perfect *Meteor*<img class="spell-icon" src="Images/SpellIcons/meteor.png" /> hit. If you really need to escape then don't, because Meteor will dispel *Incapacitate*.

## Venator ##

**Lore**

Little is known about the origins of this mysterious werebeast. Perhaps the
result of a magical curse or a forbidden experiment, it now roams the lands
to satisfy its endless thirst for blood. There are no reports of anyone
surviving after coming in close contact with the Venator, as it has
terrifying abilities to stop its prey from escaping.

**Description**

Venator focuses on getting close to its enemies and not letting them escape easily while it deals damage. Its abilities also steal life. While not completely helpless at long range with its dagger, Venator should aim to get in melee range.

### Skills ###

#### <img class="spell-icon" src="Images/SpellIcons/rend.png" />M1: Rend ####
Rapidly hits enemies at melee range in the target direction. Extremely short cooldown.

#### <img class="spell-icon" src="Images/SpellIcons/damaging_dagger.png" />M2: Damaging Dagger (primary) ####
Shoots a projectile that applies **slow** on hit. Medium cooldown.

#### <img class="spell-icon" src="Images/SpellIcons/damaging_dagger.png" />M2: Numbing Dagger (secondary) ####
Exactly like Damaging Dagger but instead of containing slow, it contains **numb** debuff.

#### <img class="spell-icon" src="Images/SpellIcons/feral_scream.png" />Q: Feral Scream ####
Causes enemies in the target direction to uncontrollably run away in fear. Long cooldown.

#### <img class="spell-icon" src="Images/SpellIcons/deep_wounds.png" />E: Deep Wounds ####
Makes short charge in the target direction that does damage and inflicts enemy hit with a bleed effect that drains life whenever they move. Long cooldown.

#### <img class="spell-icon" src="Images/SpellIcons/survival_instinct.png" />R: Survival Instinct (primary) ####
Venator's survival instinct gives it what it needs to survive. The more health Venator has, the more it does damage. The weaker the Venator is, the faster Venator moves. Long cooldown.

#### <img class="spell-icon" src="Images/SpellIcons/survival_instinct.png" />R: Blood Frenzy (secondary) ####
Venator's lust for blood gives Venator extremely strong **lifesteal** buff but Venator's spells lose some accuracy, causing them to do less damage.

#### <img class="spell-icon" src="Images/SpellIcons/leap.png" />Space: Leap ####
Leap into the target location and **incapacitates** enemies in the landing area. Long cooldown.

#### Passive: Blood drinker ####
Venator has base lifesteal that cannot be dispelled.

### Tips ###
- If you are low on health, you can try following hit-and-run combo against single targets:
  - Steps:
     1. (Optional) Use either of your R spells <img class="spell-icon" src="Images/SpellIcons/survival_instinct.png" /> to boost your combo.
     2. Use *Leap* <img class="spell-icon" src="Images/SpellIcons/leap.png" /> to get close to your enemy and *incapacitate* them.
     3. Use *Deep Wounds* <img class="spell-icon" src="Images/SpellIcons/deep_wounds.png" /> to apply *Bleed* debuff on them.
     4. Use *Feral Scream* <img class="spell-icon" src="Images/SpellIcons/feral_scream.png" /> to *Fear* your enemy. Bleed DOES NOT interrupt Fear.
     5. Just before Fear ends, throw *Damaging Dagger* <img class="spell-icon" src="Images/SpellIcons/damaging_dagger.png" /> on your target.
  - If executed successfully, combo does lot of damage and because of your *lifesteal*, you end up healthier.

- Variation of previous combo is to start melee attacking (M1) your target after *Deep Wounds* <img class="spell-icon" src="Images/SpellIcons/deep_wounds.png" />. Save *Feral Scream* <img class="spell-icon" src="Images/SpellIcons/feral_scream.png" /> for later. This combo might be more effective if you are healthy.

- If you are low on health and need to escape *Survival Instinct (R primary)* <img class="spell-icon" src="Images/SpellIcons/survival_instinct.png" /> is very useful. It gives you lot of movement speed if you have low health.


## Rock Golem ##

**Lore**

The Rock Golem is an ancient construct built for protecting sacred places
from the forces of evil. After the destruction of its temple, this Rock Golem
has gone on a journey to vanquish evil and to find a new place to
protect. Perhaps, after journeying for enough time, it might once again be
able to fulfill its original purpose by finding something uncorrupted and
worth protecting

**Description**

Rock Golem is melee hero that focuses on getting close enough to its enemies that it can chain several abilities together for a devastating effect. Rock Golem is capable of taking lots of damage with its high health and protective spells.

### Skills ###

#### <img class="spell-icon" src="Images/SpellIcons/StoneFist.png" />M1: Stone Fist ####
Hits enemies at melee range in the target direction. Short cooldown.

#### <img class="spell-icon" src="Images/SpellIcons/SealingBoulder.png" />M2: Sealing Boulder ####
Shoots a projectile with *petrify* debuff in the target direction. Medium cooldown.

#### <img class="spell-icon" src="Images/SpellIcons/SpiritStone.png" />Q: Spirit Stone (primary) ####
Creates a stone at the target location that can be either punched (M1) or tossed (E) in the target direction/location. It also blocks any attacks and gives small boost to movement speed to friendly
players in certain range. Long cooldown.

#### <img class="spell-icon" src="Images/SpellIcons/SpiritStone.png" />Q: Angry Spirit Stone (secondary) ####
Exactly like Spirit Stone but shorter duration, shorter range of influence but has slow more powerful than Spirit Stone's speed boost.

#### <img class="spell-icon" src="Images/SpellIcons/Toss.png" />E: Toss ####
If an player or a Spirit Stone is in (approximately) melee range, tosses it into the target location. Both the tossed enemy and any nearby enemies take damage upon landing. Long cooldown.

#### <img class="spell-icon" src="Images/SpellIcons/MineralArmor.png" />R: Mineral Armor (primary) ####
Gives the Rock Golem *armor* and *heal over time* buffs for a short period. The power of heal is dependent on how much armor there's left. Can be cast on allied players. Long cooldown.

#### <img class="spell-icon" src="Images/SpellIcons/MineralArmor.png" />R: Bedrock (secondary) ####
Instead of gaining *heal over time*, Rock Golem gains high amount of *armor* that absorbs most the damage received. Also reduces Rock Golem's movement speed considerably for a short period.

#### <img class="spell-icon" src="Images/SpellIcons/EarthQuake.png" />Space: Earthquake ####
Charges rapidly towards the target direction until hitting an enemy or object (or after charging maximum range). Incapacitates and damages enemies in the collision area. Long cooldown.

#### Passive ####
Rock Golem doesn't have passive at the moment.

### Tips ###
- Hitting enemy with *Sealing Boulder* gives you best possible opportunity to punch *Spirit Stone* on them. For example, one of the deadliest single target combos of *Rock Golem* is following:
  1. Use *Earthquake* <img class="spell-icon" src="Images/SpellIcons/EarthQuake.png" /> to get close to your enemy and for initial damage.
  2. Quickly throw *Sealing Boulder* <img class="spell-icon" src="Images/SpellIcons/SealingBoulder.png" /> to *Petrify* (and it does some damage too).
  3. Put *Spirit Stone* <img class="spell-icon" src="Images/SpellIcons/SpiritStone.png" /> between you and your enemy.
  4. Wait just a little bit and then punch <img class="spell-icon" src="Images/SpellIcons/StoneFist.png" /> the stone so that it hits enemy right after Petrify ends.
  5. Usually enemy escapes at this point but with some luck you might be able to toss  <img class="spell-icon" src="Images/SpellIcons/Toss.png" /> something on them or toss enemy somewhere for extra damage.

- If fighting against ranged hero that you can't reach conveniently, use either of your R-spells and toss the resulting stone to enemy.

- When escaping, you can often *Toss* <img class="spell-icon" src="Images/SpellIcons/Toss.png" />enemy behind wall.

- It's often futile to try to run away from Venator that has reached melee range since you are so slow. Venator will do free damage to you and heal itself with *lifesteal*. If you can't use any other spells, put up good fight and use *Stone Fist* <img class="spell-icon" src="Images/SpellIcons/StoneFist.png" />! After your cooldowns are over, you may either escape or try to kill Venator with one of your deadly combos.


## Elite Soldier ##

**Lore**

With his cunning, extreme speed and arsenal of weapons, Grok the Elite
Soldier has single-handedly thwarted multiple invasions of aliens and demons
alike. Using a dimensional doorway, he has come to stop chaos and evil once
again and ultimately, to prove his superior elite skills.

**Description**

Elite Soldier is a versatile hero that can pursue various strategies
depending on the situation. He can pursue his enemies in medium range
with his Shotgun, Rocket Launcher and Plasmagun or keep his distance
and snipe enemies with his Railgun and Plasma Grenades.

Unique aspect of Elite Soldier is that he has two different cooldown
systems. Most of Elite Soldier's offensive spells have short cooldown,
but they also consume ammunition. Because of this, Elite Soldier can
do high damage by repeatedly casting his more powerful spells.


### Skills ###

#### <img class="spell-icon" src="Images/SpellIcons/shotgun.png" />M1: Shotgun ####
Shoots a large amount of projectiles in the target direction at medium range. Short cooldown with limited. Consumes *Pellets*.

#### <img class="spell-icon" src="Images/SpellIcons/railgun.png" />M2: Railgun (primary) ####
Shoots a very damaging and long-range laser beam in the target direction. Long cooldown.

**Important note** Though railgun shot's properties resemble those of projectile's, it isn't actually projectile. It can go through multiple enemies and it is not absorbed by Ember Mage's shield.

#### <img class="spell-icon" src="Images/SpellIcons/railgun.png" />M2: Blinding Ray (secondary) ####
Instead of dealing high damage, blinds the enemy (making him unable to see far) for a short period.

#### <img class="spell-icon" src="Images/SpellIcons/plasma.png" />Q: Plasmagun (primary) ####
Shoots three plasma balls with a slowing effect in the target direction at medium range. Short cooldown. Consumes three *Plasma* units.

#### <img class="spell-icon" src="Images/SpellIcons/plasma.png" />Q: Plasma grenades (secondary) ####
Instead of shooting plasma balls in a straight line, shoot three plasma grenades in an arc over the air. Consumes three *Plasma* units.

#### <img class="spell-icon" src="Images/SpellIcons/rocket_launcher.png" />E: Rocket Launcher ####
Shoots projectile that does medium damage and has powerful knockback in the target direction at medium range. Short cooldown. Consumes one *Rocket*.

#### <img class="spell-icon" src="Images/SpellIcons/like_a_pro.png" />R: Like a Pro ####
Gives the Elite Soldier the ability to cast his spells while moving, *Speed* buff, *Armor* buff and immediately gives some ammunition to all weapons. Long cooldown.

#### <img class="spell-icon" src="Images/SpellIcons/rocket_jump.jpeg" />Space: Rocket Jump ####
Jump into the target location and deal damage to nearby enemies upon jumping. Long cooldown. Consumes one *Rocket*.

#### Passive: Collect ammunition ####
Elite soldier collects ammunition over time.

- **Pellets:** Required by *Shotgun*. Short reloading time.
- **Plasma:** Required by *Plasmagun* and *Plasma grenades*. Medium reloading time.
- **Rockets:** Required by *Rocket Launcher* and *Rocket Jump*. Long reloading time.


## Shadowmancer ##

### Lore ###
Though goblins are usually considered weak, few mortals can be
considered as formidable as Buruk, the Shadowmancer. Buruk used to protect
his tribe from slavers until it was slaughtered in the First Great
Purge. Buruk now wanders around the realms, occasionally teaching lessons to
slavers, fanatics and others who have mistaken him for easy prey.

**Description**

Shadowmancer is a ranged support-dps hero. While Shadowmancer has low
hp and is easily knocked back, his disables and protection spells are
more than enough to stop most attacks.

### Skills ###

#### <img class="spell-icon" src="Images/SpellIcons/ShadowOrb.png" />M1: Shadow Orb ####
Shoots a medium range projectile. Very low cooldown.

#### <img class="spell-icon" src="Images/SpellIcons/DarkEnergy.png" />M2: Dark Energy (primary) ####
Healing spell that gives *Heal over time* to friendly units on the target area and a small amount of armor. Very low cooldown.

#### <img class="spell-icon" src="Images/SpellIcons/DarkEnergy.png" />M2: Dark Energy (secondary) ####
Like normal Dark Energy, but always throws projectile at Shadowmancer's location.

#### <img class="spell-icon" src="Images/SpellIcons/Drain.png" />Q: Drain (primary) ####
Shoots a medium range proctile that *Silences* target and gives *Drain* debuff.

*Drain*: When a character has this debuff, it loses health and movement speed over time and gives that to the Shadowmancer who applied the debuff. If character is far enough from the Shadowmancer, drain stops affecting until it's close enough again.

#### <img class="spell-icon" src="Images/SpellIcons/Drain.png" />Q: Drain (secondary) ####
Like the primary Drain, but with shorter cast time and faster projectile speed. However, duration is much shorter.

#### <img class="spell-icon" src="Images/SpellIcons/VoidSpear.png" />E: Void Spear ####
Shoots long range projectile that makes the more damage the farther it has flown. Applies a debuff on hit that decreases movement speed if target moves.

#### <img class="spell-icon" src="Images/SpellIcons/Shadow.png" />R: Shadow ####
Makes a target friendly unit immaterial, meaning that it can't be hit with most spells and it can move through characters but on a flip side can't cast spells during that time.

#### <img class="spell-icon" src="Images/SpellIcons/IntoTheShadows.png" />Space: Into the Shadows ####
Shadowmancer disappears and after a small delay, appears to target location, damaging and knocking back nearby enemy characters.

#### Passive ####
No passive.

### Tips ###
- As a support hero, you must stay aware of your team mates situations.
- If you are threatened by a melee character, save *Drain* <img class="spell-icon" src="Images/SpellIcons/Drain.png" /> for defensive purposes.
- Shadowmancer's small size makes him highly susceptible to knockbacks. Shadowmancer should probably stay far away from lava. *Shadow* <img class="spell-icon" src="Images/SpellIcons/Shadow.png" /> does NOT protect from lava damage.
- *Into the Shadow*'s<img class="spell-icon" src="Images/SpellIcons/IntoTheShadows.png" /> is great for offensive use because its knockback makes it hard to execute a swift counter attack.
- It can be hard to hit *Drain*<img class="spell-icon" src="Images/SpellIcons/Drain.png" /> because of its slow speed. If you are on the defensive, one good trick is to run behind a corner and shoot *Drain* immediately if you see the enemy following you.