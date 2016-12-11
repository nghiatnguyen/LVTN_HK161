/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package coreferenceresolver.process;

import coreferenceresolver.element.CRFToken;
import coreferenceresolver.util.StanfordUtil;
import coreferenceresolver.util.Util;
import coreferenceresolver.element.NounPhrase;
import coreferenceresolver.element.Token;
import coreferenceresolver.element.Review;
import coreferenceresolver.element.Sentence;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import edu.stanford.nlp.trees.Tree;
import java.io.File;
import java.io.IOException;
import org.apache.commons.io.FileUtils;

/**
 *
 * @author TRONGNGHIA
 */
public class FeatureExtractor {

    private static final String SINGULAR_KEYWORDS = ";it;this;that;one;";
    private static final String NOT_OBJECTS = ";this;that;these;those;what;which;where;";
    private static final String[] TO_BES = {"is", "'s", "are", "'re", "was", "were", "been", "be"};
    private static final String[] SPECIAL_COMPARATIVES = {"inferior", "superior", "junior", "senior", "anterior", "posterior", "prior"};
    private static final String ADJECTIVE_TAG = "JJ";
    private static final String COMPARATIVE_ADJECTIVE_TAG = "JJR";
    private static final String COMPARATIVE_ADVERB_TAG = "RBR";
    private static final String ADVERB_TAG = "RB";
    private static final String SINGULAR_NOUN_TAG = "NN";
    private static final String PLURAL_NOUN_TAG = "NNS";
    public static final String COMPARATIVE_VERBS = ";beat;beats;win;wins;outperform;outperforms;outperformed;";
    private static final String[] WORDS = Util.getDataset() != null ? Util.getDataset().split("\\.") : null;
    //List of Pronouns include reflexive pronouns, personal pronouns and possessive pronouns.
    private static final ArrayList<String> PRONOUNS = new ArrayList<String>(
            Arrays.asList("i", "you", "he", "she", "it", "we", "you", "they",
                    "myself", "yourself", "himself", "herself", "itself",
                    "ourselves", "yourselves", "themselves", "mine", "yours",
                    "hers", "his", "ours", "yours", "theirs",
                    "me", "you", "him", "her", "us", "them"));
    private static final ArrayList<String> DETERMINERS = new ArrayList<String>(
            Arrays.asList("this", "that", "these", "those", "a", "an", "the", "my", "your", "its", "her", "his", "their", "our"));
    private static final ArrayList<String> listTOBE = new ArrayList<String>(Arrays.asList("is", "'s", "are", "'re", "was", "were", "been", "be"));
    private static String sDict = "";
    //The list of negative words
    public static String NEGATIVE_WORDS = ";2-faced;2-faces;abnormal;abolish;abominable;abominably;abominate;abomination;abort;aborted;aborts;abrade;abrasive;abrupt;abruptly;abscond;absence;absent-minded;absentee;absurd;absurdity;absurdly;absurdness;abuse;abused;abuses;abusive;abysmal;abysmally;abyss;accidental;accost;accursed;accusation;accusations;accuse;accuses;accusing;accusingly;acerbate;acerbic;acerbically;ache;ached;aches;achey;aching;acrid;acridly;acridness;acrimonious;acrimoniously;acrimony;adamant;adamantly;addict;addicted;addicting;addicts;admonish;admonisher;admonishingly;admonishment;admonition;adulterate;adulterated;adulteration;adulterier;adversarial;adversary;adverse;adversity;afflict;affliction;afflictive;affront;afraid;aggravate;aggravating;aggravation;aggression;aggressive;aggressiveness;aggressor;aggrieve;aggrieved;aggrivation;aghast;agonies;agonize;agonizing;agonizingly;agony;aground;ail;ailing;ailment;aimless;alarm;alarmed;alarming;alarmingly;alienate;alienated;alienation;allegation;allegations;allege;allergic;allergies;allergy;aloof;altercation;ambiguity;ambiguous;ambivalence;ambivalent;ambush;amiss;amputate;anarchism;anarchist;anarchistic;anarchy;anemic;anger;angrily;angriness;angry;anguish;animosity;annihilate;annihilation;annoy;annoyance;annoyances;annoyed;annoying;annoyingly;annoys;anomalous;anomaly;antagonism;antagonist;antagonistic;antagonize;anti-;anti-american;anti-israeli;anti-occupation;anti-proliferation;anti-semites;anti-social;anti-us;anti-white;antipathy;antiquated;antithetical;anxieties;anxiety;anxious;anxiously;anxiousness;apathetic;apathetically;apathy;apocalypse;apocalyptic;apologist;apologists;appal;appall;appalled;appalling;appallingly;apprehension;apprehensions;apprehensive;apprehensively;arbitrary;arcane;archaic;arduous;arduously;argumentative;arrogance;arrogant;arrogantly;ashamed;asinine;asininely;asinininity;askance;asperse;aspersion;aspersions;assail;assassin;assassinate;assault;assult;astray;asunder;atrocious;atrocities;atrocity;atrophy;attack;attacks;audacious;audaciously;audaciousness;audacity;audiciously;austere;authoritarian;autocrat;autocratic;avalanche;avarice;avaricious;avariciously;avenge;averse;aversion;aweful;awful;awfully;awfulness;awkward;awkwardness;ax;babble;back-logged;back-wood;back-woods;backache;backaches;backaching;backbite;backbiting;backward;backwardness;backwood;backwoods;bad;badly;baffle;baffled;bafflement;baffling;bait;balk;banal;banalize;bane;banish;banishment;bankrupt;barbarian;barbaric;barbarically;barbarity;barbarous;barbarously;barren;baseless;bash;bashed;bashful;bashing;bastard;bastards;battered;battering;batty;bearish;beastly;bedlam;bedlamite;befoul;beg;beggar;beggarly;begging;beguile;belabor;belated;beleaguer;belie;belittle;belittled;belittling;bellicose;belligerence;belligerent;belligerently;bemoan;bemoaning;bemused;bent;berate;bereave;bereavement;bereft;berserk;beseech;beset;besiege;besmirch;bestial;betray;betrayal;betrayals;betrayer;betraying;betrays;bewail;beware;bewilder;bewildered;bewildering;bewilderingly;bewilderment;bewitch;bias;biased;biases;bicker;bickering;bid-rigging;bigotries;bigotry;bitch;bitchy;biting;bitingly;bitter;bitterly;bitterness;bizarre;blab;blabber;blackmail;blah;blame;blameworthy;bland;blandish;blaspheme;blasphemous;blasphemy;blasted;blatant;blatantly;blather;bleak;bleakly;bleakness;bleed;bleeding;bleeds;blemish;blind;blinding;blindingly;blindside;blister;blistering;bloated;blockage;blockhead;bloodshed;bloodthirsty;bloody;blotchy;blow;blunder;blundering;blunders;blunt;blur;bluring;blurred;blurring;blurry;blurs;blurt;boastful;boggle;bogus;boil;boiling;boisterous;bomb;bombard;bombardment;bombastic;bondage;bonkers;bore;bored;boredom;bores;boring;botch;bother;bothered;bothering;bothers;bothersome;bowdlerize;boycott;braggart;bragger;brainless;brainwash;brash;brashly;brashness;brat;bravado;brazen;brazenly;brazenness;breach;break;break-up;break-ups;breakdown;breaking;breaks;breakup;breakups;bribery;brimstone;bristle;brittle;broke;broken;broken-hearted;brood;browbeat;bruise;bruised;bruises;bruising;brusque;brutal;brutalising;brutalities;brutality;brutalize;brutalizing;brutally;brute;brutish;bs;buckle;bug;bugging;buggy;bugs;bulkier;bulkiness;bulky;bulkyness;bull****;bull----;bullies;bullshit;bullshyt;bully;bullying;bullyingly;bum;bump;bumped;bumping;bumpping;bumps;bumpy;bungle;bungler;bungling;bunk;burden;burdensome;burdensomely;burn;burned;burning;burns;bust;busts;busybody;butcher;butchery;buzzing;byzantine;cackle;calamities;calamitous;calamitously;calamity;callous;calumniate;calumniation;calumnies;calumnious;calumniously;calumny;cancer;cancerous;cannibal;cannibalize;capitulate;capricious;capriciously;capriciousness;capsize;careless;carelessness;caricature;carnage;carp;cartoonish;cash-strapped;castigate;castrated;casualty;cataclysm;cataclysmal;cataclysmic;cataclysmically;catastrophe;catastrophes;catastrophic;catastrophically;catastrophies;caustic;caustically;cautionary;cave;censure;chafe;chaff;chagrin;challenging;chaos;chaotic;chasten;chastise;chastisement;chatter;chatterbox;cheap;cheapen;cheaply;cheat;cheated;cheater;cheating;cheats;checkered;cheerless;cheesy;chide;childish;chill;chilly;chintzy;choke;choleric;choppy;chore;chronic;chunky;clamor;clamorous;clash;cliche;cliched;clique;clog;clogged;clogs;cloud;clouding;cloudy;clueless;clumsy;clunky;coarse;cocky;coerce;coercion;coercive;cold;coldly;collapse;collude;collusion;combative;combust;comical;commiserate;commonplace;commotion;commotions;complacent;complain;complained;complaining;complains;complaint;complaints;complex;complicated;complication;complicit;compulsion;compulsive;concede;conceded;conceit;conceited;concen;concens;concern;concerned;concerns;concession;concessions;condemn;condemnable;condemnation;condemned;condemns;condescend;condescending;condescendingly;condescension;confess;confession;confessions;confined;conflict;conflicted;conflicting;conflicts;confound;confounded;confounding;confront;confrontation;confrontational;confuse;confused;confuses;confusing;confusion;confusions;congested;congestion;cons;conscons;conservative;conspicuous;conspicuously;conspiracies;conspiracy;conspirator;conspiratorial;conspire;consternation;contagious;contaminate;contaminated;contaminates;contaminating;contamination;contempt;contemptible;contemptuous;contemptuously;contend;contention;contentious;contort;contortions;contradict;contradiction;contradictory;contrariness;contravene;contrive;contrived;controversial;controversy;convoluted;corrode;corrosion;corrosions;corrosive;corrupt;corrupted;corrupting;corruption;corrupts;corruptted;costlier;costly;counter-productive;counterproductive;coupists;covetous;coward;cowardly;crabby;crack;cracked;cracks;craftily;craftly;crafty;cramp;cramped;cramping;cranky;crap;crappy;craps;crash;crashed;crashes;crashing;crass;craven;cravenly;craze;crazily;craziness;crazy;creak;creaking;creaks;credulous;creep;creeping;creeps;creepy;crept;crime;criminal;cringe;cringed;cringes;cripple;crippled;cripples;crippling;crisis;critic;critical;criticism;criticisms;criticize;criticized;criticizing;critics;cronyism;crook;crooked;crooks;crowded;crowdedness;crude;cruel;crueler;cruelest;cruelly;cruelness;cruelties;cruelty;crumble;crumbling;crummy;crumple;crumpled;crumples;crush;crushed;crushing;cry;culpable;culprit;cumbersome;cunt;cunts;cuplrit;curse;cursed;curses;curt;cuss;cussed;cutthroat;cynical;cynicism;d*mn;damage;damaged;damages;damaging;damn;damnable;damnably;damnation;damned;damning;damper;danger;dangerous;dangerousness;dark;darken;darkened;darker;darkness;dastard;dastardly;daunt;daunting;dauntingly;dawdle;daze;dazed;dead;deadbeat;deadlock;deadly;deadweight;deaf;dearth;death;debacle;debase;debasement;debaser;debatable;debauch;debaucher;debauchery;debilitate;debilitating;debility;debt;debts;decadence;decadent;decay;decayed;deceit;deceitful;deceitfully;deceitfulness;deceive;deceiver;deceivers;deceiving;deception;deceptive;deceptively;declaim;decline;declines;declining;decrement;decrepit;decrepitude;decry;defamation;defamations;defamatory;defame;defect;defective;defects;defensive;defiance;defiant;defiantly;deficiencies;deficiency;deficient;defile;defiler;deform;deformed;defrauding;defunct;defy;degenerate;degenerately;degeneration;degradation;degrade;degrading;degradingly;dehumanization;dehumanize;deign;deject;dejected;dejectedly;dejection;delay;delayed;delaying;delays;delinquency;delinquent;delirious;delirium;delude;deluded;deluge;delusion;delusional;delusions;demean;demeaning;demise;demolish;demolisher;demon;demonic;demonize;demonized;demonizes;demonizing;demoralize;demoralizing;demoralizingly;denial;denied;denies;denigrate;denounce;dense;dent;dented;dents;denunciate;denunciation;denunciations;deny;denying;deplete;deplorable;deplorably;deplore;deploring;deploringly;deprave;depraved;depravedly;deprecate;depress;depressed;depressing;depressingly;depression;depressions;deprive;deprived;deride;derision;derisive;derisively;derisiveness;derogatory;desecrate;desert;desertion;desiccate;desiccated;desititute;desolate;desolately;desolation;despair;despairing;despairingly;desperate;desperately;desperation;despicable;despicably;despise;despised;despoil;despoiler;despondence;despondency;despondent;despondently;despot;despotic;despotism;destabilisation;destains;destitute;destitution;destroy;destroyer;destruction;destructive;desultory;deter;deteriorate;deteriorating;deterioration;deterrent;detest;detestable;detestably;detested;detesting;detests;detract;detracted;detracting;detraction;detracts;detriment;detrimental;devastate;devastated;devastates;devastating;devastatingly;devastation;deviate;deviation;devil;devilish;devilishly;devilment;devilry;devious;deviously;deviousness;devoid;diabolic;diabolical;diabolically;diametrically;diappointed;diatribe;diatribes;dick;dictator;dictatorial;die;die-hard;died;dies;difficult;difficulties;difficulty;diffidence;dilapidated;dilemma;dilly-dally;dim;dimmer;din;ding;dings;dinky;dire;direly;direness;dirt;dirtbag;dirtbags;dirts;dirty;disable;disabled;disaccord;disadvantage;disadvantaged;disadvantageous;disadvantages;disaffect;disaffected;disaffirm;disagree;disagreeable;disagreeably;disagreed;disagreeing;disagreement;disagrees;disallow;disapointed;disapointing;disapointment;disappoint;disappointed;disappointing;disappointingly;disappointment;disappointments;disappoints;disapprobation;disapproval;disapprove;disapproving;disarm;disarray;disaster;disasterous;disastrous;disastrously;disavow;disavowal;disbelief;disbelieve;disbeliever;disclaim;discombobulate;discomfit;discomfititure;discomfort;discompose;disconcert;disconcerted;disconcerting;disconcertingly;disconsolate;disconsolately;disconsolation;discontent;discontented;discontentedly;discontinued;discontinuity;discontinuous;discord;discordance;discordant;discountenance;discourage;discouragement;discouraging;discouragingly;discourteous;discourteously;discoutinous;discredit;discrepant;discriminate;discrimination;discriminatory;disdain;disdained;disdainful;disdainfully;disfavor;disgrace;disgraced;disgraceful;disgracefully;disgruntle;disgruntled;disgust;disgusted;disgustedly;disgustful;disgustfully;disgusting;disgustingly;dishearten;disheartening;dishearteningly;dishonest;dishonestly;dishonesty;dishonor;dishonorable;dishonorablely;disillusion;disillusioned;disillusionment;disillusions;disinclination;disinclined;disingenuous;disingenuously;disintegrate;disintegrated;disintegrates;disintegration;disinterest;disinterested;dislike;disliked;dislikes;disliking;dislocated;disloyal;disloyalty;dismal;dismally;dismalness;dismay;dismayed;dismaying;dismayingly;dismissive;dismissively;disobedience;disobedient;disobey;disoobedient;disorder;disordered;disorderly;disorganized;disorient;disoriented;disown;disparage;disparaging;disparagingly;dispensable;dispirit;dispirited;dispiritedly;dispiriting;displace;displaced;displease;displeased;displeasing;displeasure;disproportionate;disprove;disputable;dispute;disputed;disquiet;disquieting;disquietingly;disquietude;disregard;disregardful;disreputable;disrepute;disrespect;disrespectable;disrespectablity;disrespectful;disrespectfully;disrespectfulness;disrespecting;disrupt;disruption;disruptive;diss;dissapointed;dissappointed;dissappointing;dissatisfaction;dissatisfactory;dissatisfied;dissatisfies;dissatisfy;dissatisfying;dissed;dissemble;dissembler;dissension;dissent;dissenter;dissention;disservice;disses;dissidence;dissident;dissidents;dissing;dissocial;dissolute;dissolution;dissonance;dissonant;dissonantly;dissuade;dissuasive;distains;distaste;distasteful;distastefully;distort;distorted;distortion;distorts;distract;distracting;distraction;distraught;distraughtly;distraughtness;distress;distressed;distressing;distressingly;distrust;distrustful;distrusting;disturb;disturbance;disturbed;disturbing;disturbingly;disunity;disvalue;divergent;divisive;divisively;divisiveness;dizzing;dizzingly;dizzy;doddering;dodgey;dogged;doggedly;dogmatic;doldrums;domineer;domineering;donside;doom;doomed;doomsday;dope;doubt;doubtful;doubtfully;doubts;douchbag;douchebag;douchebags;downbeat;downcast;downer;downfall;downfallen;downgrade;downhearted;downheartedly;downhill;downside;downsides;downturn;downturns;drab;draconian;draconic;drag;dragged;dragging;dragoon;drags;drain;drained;draining;drains;drastic;drastically;drawback;drawbacks;dread;dreadful;dreadfully;dreadfulness;dreary;dripped;dripping;drippy;drips;drones;droop;droops;drop-out;drop-outs;dropout;dropouts;drought;drowning;drunk;drunkard;drunken;dubious;dubiously;dubitable;dud;dull;dullard;dumb;dumbfound;dump;dumped;dumping;dumps;dunce;dungeon;dungeons;dupe;dust;dusty;dwindling;dying;earsplitting;eccentric;eccentricity;effigy;effrontery;egocentric;egomania;egotism;egotistical;egotistically;egregious;egregiously;election-rigger;elimination;emaciated;emasculate;embarrass;embarrassing;embarrassingly;embarrassment;embattled;embroil;embroiled;embroilment;emergency;emphatic;emphatically;emptiness;encroach;encroachment;endanger;enemies;enemy;enervate;enfeeble;enflame;engulf;enjoin;enmity;enrage;enraged;enraging;enslave;entangle;entanglement;entrap;entrapment;envious;enviously;enviousness;epidemic;equivocal;erase;erode;erodes;erosion;err;errant;erratic;erratically;erroneous;erroneously;error;errors;eruptions;escapade;eschew;estranged;evade;evasion;evasive;evil;evildoer;evils;eviscerate;exacerbate;exagerate;exagerated;exagerates;exaggerate;exaggeration;exasperate;exasperated;exasperating;exasperatingly;exasperation;excessive;excessively;exclusion;excoriate;excruciating;excruciatingly;excuse;excuses;execrate;exhaust;exhausted;exhaustion;exhausts;exhorbitant;exhort;exile;exorbitant;exorbitantance;exorbitantly;expel;expensive;expire;expired;explode;exploit;exploitation;explosive;expropriate;expropriation;expulse;expunge;exterminate;extermination;extinguish;extort;extortion;extraneous;extravagance;extravagant;extravagantly;extremism;extremist;extremists;eyesore;f**k;fabricate;fabrication;facetious;facetiously;fail;failed;failing;fails;failure;failures;faint;fainthearted;faithless;fake;fall;fallacies;fallacious;fallaciously;fallaciousness;fallacy;fallen;falling;fallout;falls;false;falsehood;falsely;falsify;falter;faltered;famine;famished;fanatic;fanatical;fanatically;fanaticism;fanatics;fanciful;far-fetched;farce;farcical;farcical-yet-provocative;farcically;farfetched;fascism;fascist;fastidious;fastidiously;fastuous;fat;fat-cat;fat-cats;fatal;fatalistic;fatalistically;fatally;fatcat;fatcats;fateful;fatefully;fathomless;fatigue;fatigued;fatique;fatty;fatuity;fatuous;fatuously;fault;faults;faulty;fawningly;faze;fear;fearful;fearfully;fears;fearsome;feckless;feeble;feeblely;feebleminded;feign;feint;felon;felonious;ferociously;ferocity;fetid;fever;feverish;fevers;fiasco;fib;fibber;fickle;fiction;fictional;fictitious;fidget;fidgety;fiend;fiendish;fierce;figurehead;filth;filthy;finagle;finicky;fissures;fist;flabbergast;flabbergasted;flagging;flagrant;flagrantly;flair;flairs;flak;flake;flakey;flakieness;flaking;flaky;flare;flares;flareup;flareups;flat-out;flaunt;flaw;flawed;flaws;flee;fleed;fleeing;fleer;flees;fleeting;flicering;flicker;flickering;flickers;flighty;flimflam;flimsy;flirt;flirty;floored;flounder;floundering;flout;fluster;foe;fool;fooled;foolhardy;foolish;foolishly;foolishness;forbid;forbidden;forbidding;forceful;foreboding;forebodingly;forfeit;forged;forgetful;forgetfully;forgetfulness;forlorn;forlornly;forsake;forsaken;forswear;foul;foully;foulness;fractious;fractiously;fracture;fragile;fragmented;frail;frantic;frantically;franticly;fraud;fraudulent;fraught;frazzle;frazzled;freak;freaking;freakish;freakishly;freaks;freeze;freezes;freezing;frenetic;frenetically;frenzied;frenzy;fret;fretful;frets;friction;frictions;fried;friggin;frigging;fright;frighten;frightening;frighteningly;frightful;frightfully;frigid;frost;frown;froze;frozen;fruitless;fruitlessly;frustrate;frustrated;frustrates;frustrating;frustratingly;frustration;frustrations;fuck;fucking;fudge;fugitive;full-blown;fulminate;fumble;fume;fumes;fundamentalism;funky;funnily;funny;furious;furiously;furor;fury;fuss;fussy;fustigate;fusty;futile;futilely;futility;fuzzy;gabble;gaff;gaffe;gainsay;gainsayer;gall;galling;gallingly;galls;gangster;gape;garbage;garish;gasp;gauche;gaudy;gawk;gawky;geezer;genocide;get-rich;ghastly;ghetto;ghosting;gibber;gibberish;gibe;giddy;gimmick;gimmicked;gimmicking;gimmicks;gimmicky;glare;glaringly;glib;glibly;glitch;glitches;gloatingly;gloom;gloomy;glower;glum;glut;gnawing;goad;goading;god-awful;goof;goofy;goon;gossip;graceless;gracelessly;graft;grainy;grapple;grate;grating;gravely;greasy;greed;greedy;grief;grievance;grievances;grieve;grieving;grievous;grievously;grim;grimace;grind;gripe;gripes;grisly;gritty;gross;grossly;grotesque;grouch;grouchy;groundless;grouse;growl;grudge;grudges;grudging;grudgingly;gruesome;gruesomely;gruff;grumble;grumpier;grumpiest;grumpily;grumpish;grumpy;guile;guilt;guiltily;guilty;gullible;gutless;gutter;hack;hacks;haggard;haggle;hairloss;halfhearted;halfheartedly;hallucinate;hallucination;hamper;hampered;handicapped;hang;hangs;haphazard;hapless;harangue;harass;harassed;harasses;harassment;harboring;harbors;hard;hard-hit;hard-line;hard-liner;hardball;harden;hardened;hardheaded;hardhearted;hardliner;hardliners;hardship;hardships;harm;harmed;harmful;harms;harpy;harridan;harried;harrow;harsh;harshly;hasseling;hassle;hassled;hassles;haste;hastily;hasty;hate;hated;hateful;hatefully;hatefulness;hater;haters;hates;hating;hatred;haughtily;haughty;haunt;haunting;havoc;hawkish;haywire;hazard;hazardous;haze;hazy;head-aches;headache;headaches;heartbreaker;heartbreaking;heartbreakingly;heartless;heathen;heavy-handed;heavyhearted;heck;heckle;heckled;heckles;hectic;hedge;hedonistic;heedless;hefty;hegemonism;hegemonistic;hegemony;heinous;hell;hell-bent;hellion;hells;helpless;helplessly;helplessness;heresy;heretic;heretical;hesitant;hestitant;hideous;hideously;hideousness;high-priced;hiliarious;hinder;hindrance;hiss;hissed;hissing;ho-hum;hoard;hoax;hobble;hogs;hollow;hoodium;hoodwink;hooligan;hopeless;hopelessly;hopelessness;horde;horrendous;horrendously;horrible;horrid;horrific;horrified;horrifies;horrify;horrifying;horrifys;hostage;hostile;hostilities;hostility;hot;hotbeds;hothead;hotheaded;hothouse;hubris;huckster;hum;humid;humiliate;humiliating;humiliation;humming;hung;hurt;hurted;hurtful;hurting;hurts;hustler;hype;hypocricy;hypocrisy;hypocrite;hypocrites;hypocritical;hypocritically;hysteria;hysteric;hysterical;hysterically;hysterics;idiocies;idiocy;idiot;idiotic;idiotically;idiots;idle;ignoble;ignominious;ignominiously;ignominy;ignorance;ignorant;ignore;ill-advised;ill-conceived;ill-defined;ill-designed;ill-fated;ill-favored;ill-formed;ill-mannered;ill-natured;ill-sorted;ill-tempered;ill-treated;ill-treatment;ill-usage;ill-used;illegal;illegally;illegitimate;illicit;illiterate;illness;illogic;illogical;illogically;illusion;illusions;illusory;imaginary;imbalance;imbecile;imbroglio;immaterial;immature;imminence;imminently;immobilized;immoderate;immoderately;immodest;immoral;immorality;immorally;immovable;impair;impaired;impasse;impatience;impatient;impatiently;impeach;impedance;impede;impediment;impending;impenitent;imperfect;imperfection;imperfections;imperfectly;imperialist;imperil;imperious;imperiously;impermissible;impersonal;impertinent;impetuous;impetuously;impiety;impinge;impious;implacable;implausible;implausibly;implicate;implication;implode;impolite;impolitely;impolitic;importunate;importune;impose;imposers;imposing;imposition;impossible;impossiblity;impossibly;impotent;impoverish;impoverished;impractical;imprecate;imprecise;imprecisely;imprecision;imprison;imprisonment;improbability;improbable;improbably;improper;improperly;impropriety;imprudence;imprudent;impudence;impudent;impudently;impugn;impulsive;impulsively;impunity;impure;impurity;inability;inaccuracies;inaccuracy;inaccurate;inaccurately;inaction;inactive;inadequacy;inadequate;inadequately;inadverent;inadverently;inadvisable;inadvisably;inane;inanely;inappropriate;inappropriately;inapt;inaptitude;inarticulate;inattentive;inaudible;incapable;incapably;incautious;incendiary;incense;incessant;incessantly;incite;incitement;incivility;inclement;incognizant;incoherence;incoherent;incoherently;incommensurate;incomparable;incomparably;incompatability;incompatibility;incompatible;incompetence;incompetent;incompetently;incomplete;incompliant;incomprehensible;incomprehension;inconceivable;inconceivably;incongruous;incongruously;inconsequent;inconsequential;inconsequentially;inconsequently;inconsiderate;inconsiderately;inconsistence;inconsistencies;inconsistency;inconsistent;inconsolable;inconsolably;inconstant;inconvenience;inconveniently;incorrect;incorrectly;incorrigible;incorrigibly;incredulous;incredulously;inculcate;indecency;indecent;indecently;indecision;indecisive;indecisively;indecorum;indefensible;indelicate;indeterminable;indeterminably;indeterminate;indifference;indifferent;indigent;indignant;indignantly;indignation;indignity;indiscernible;indiscreet;indiscreetly;indiscretion;indiscriminate;indiscriminately;indiscriminating;indistinguishable;indoctrinate;indoctrination;indolent;indulge;ineffective;ineffectively;ineffectiveness;ineffectual;ineffectually;ineffectualness;inefficacious;inefficacy;inefficiency;inefficient;inefficiently;inelegance;inelegant;ineligible;ineloquent;ineloquently;inept;ineptitude;ineptly;inequalities;inequality;inequitable;inequitably;inequities;inescapable;inescapably;inessential;inevitable;inevitably;inexcusable;inexcusably;inexorable;inexorably;inexperience;inexperienced;inexpert;inexpertly;inexpiable;inexplainable;inextricable;inextricably;infamous;infamously;infamy;infected;infection;infections;inferior;inferiority;infernal;infest;infested;infidel;infidels;infiltrator;infiltrators;infirm;inflame;inflammation;inflammatory;inflammed;inflated;inflationary;inflexible;inflict;infraction;infringe;infringement;infringements;infuriate;infuriated;infuriating;infuriatingly;inglorious;ingrate;ingratitude;inhibit;inhibition;inhospitable;inhospitality;inhuman;inhumane;inhumanity;inimical;inimically;iniquitous;iniquity;injudicious;injure;injurious;injury;injustice;injustices;innuendo;inoperable;inopportune;inordinate;inordinately;insane;insanely;insanity;insatiable;insecure;insecurity;insensible;insensitive;insensitively;insensitivity;insidious;insidiously;insignificance;insignificant;insignificantly;insincere;insincerely;insincerity;insinuate;insinuating;insinuation;insociable;insolence;insolent;insolently;insolvent;insouciance;instability;instable;instigate;instigator;instigators;insubordinate;insubstantial;insubstantially;insufferable;insufferably;insufficiency;insufficient;insufficiently;insular;insult;insulted;insulting;insultingly;insults;insupportable;insupportably;insurmountable;insurmountably;insurrection;intefere;inteferes;intense;interfere;interference;interferes;intermittent;interrupt;interruption;interruptions;intimidate;intimidating;intimidatingly;intimidation;intolerable;intolerablely;intolerance;intoxicate;intractable;intransigence;intransigent;intrude;intrusion;intrusive;inundate;inundated;invader;invalid;invalidate;invalidity;invasive;invective;inveigle;invidious;invidiously;invidiousness;invisible;involuntarily;involuntary;irascible;irate;irately;ire;irk;irked;irking;irks;irksome;irksomely;irksomeness;irksomenesses;ironic;ironical;ironically;ironies;irony;irragularity;irrational;irrationalities;irrationality;irrationally;irrationals;irreconcilable;irrecoverable;irrecoverableness;irrecoverablenesses;irrecoverably;irredeemable;irredeemably;irreformable;irregular;irregularity;irrelevance;irrelevant;irreparable;irreplacible;irrepressible;irresolute;irresolvable;irresponsible;irresponsibly;irretating;irretrievable;irreversible;irritable;irritably;irritant;irritate;irritated;irritating;irritation;irritations;isolate;isolated;isolation;issue;issues;itch;itching;itchy;jabber;jaded;jagged;jam;jarring;jaundiced;jealous;jealously;jealousness;jealousy;jeer;jeering;jeeringly;jeers;jeopardize;jeopardy;jerk;jerky;jitter;jitters;jittery;job-killing;jobless;joke;joker;jolt;judder;juddering;judders;jumpy;junk;junky;junkyard;jutter;jutters;kaput;kill;killed;killer;killing;killjoy;kills;knave;knife;knock;knotted;kook;kooky;lack;lackadaisical;lacked;lackey;lackeys;lacking;lackluster;lacks;laconic;lag;lagged;lagging;laggy;lags;laid-off;lambast;lambaste;lame;lame-duck;lament;lamentable;lamentably;languid;languish;languor;languorous;languorously;lanky;lapse;lapsed;lapses;lascivious;last-ditch;latency;laughable;laughably;laughingstock;lawbreaker;lawbreaking;lawless;lawlessness;layoff;layoff-happy;lazy;leak;leakage;leakages;leaking;leaks;leaky;lech;lecher;lecherous;lechery;leech;leer;leery;left-leaning;lemon;lengthy;less-developed;lesser-known;letch;lethal;lethargic;lethargy;lewd;lewdly;lewdness;liability;liable;liar;liars;licentious;licentiously;licentiousness;lie;lied;lier;lies;life-threatening;lifeless;limit;limitation;limitations;limited;limits;limp;listless;litigious;little-known;livid;lividly;loath;loathe;loathing;loathly;loathsome;loathsomely;lone;loneliness;lonely;loner;lonesome;long-time;long-winded;longing;longingly;loophole;loopholes;loose;loot;lorn;lose;loser;losers;loses;losing;loss;losses;lost;lousy;loveless;lovelorn;low;lower;lowest;low-rated;lowly;ludicrous;ludicrously;lugubrious;lukewarm;lull;lumpy;lunatic;lunaticism;lurch;lure;lurid;lurk;lurking;lying;macabre;mad;madden;maddening;maddeningly;madder;madly;madman;madness;maladjusted;maladjustment;malady;malaise;malcontent;malcontented;maledict;malevolence;malevolent;malevolently;malice;malicious;maliciously;maliciousness;malign;malignant;malodorous;maltreatment;mangle;mangled;mangles;mangling;mania;maniac;maniacal;manic;manipulate;manipulation;manipulative;manipulators;mar;marginal;marginally;martyrdom;martyrdom-seeking;mashed;massacre;massacres;matte;mawkish;mawkishly;mawkishness;meager;meaningless;meanness;measly;meddle;meddlesome;mediocre;mediocrity;melancholy;melodramatic;melodramatically;meltdown;menace;menacing;menacingly;mendacious;mendacity;menial;merciless;mercilessly;mess;messed;messes;messing;messy;midget;miff;militancy;mindless;mindlessly;mirage;mire;misalign;misaligned;misaligns;misapprehend;misbecome;misbecoming;misbegotten;misbehave;misbehavior;miscalculate;miscalculation;miscellaneous;mischief;mischievous;mischievously;misconception;misconceptions;miscreant;miscreants;misdirection;miser;miserable;miserableness;miserably;miseries;miserly;misery;misfit;misfortune;misgiving;misgivings;misguidance;misguide;misguided;mishandle;mishap;misinform;misinformed;misinterpret;misjudge;misjudgment;mislead;misleading;misleadingly;mislike;mismanage;mispronounce;mispronounced;mispronounces;misread;misreading;misrepresent;misrepresentation;miss;missed;misses;misstatement;mist;mistake;mistaken;mistakenly;mistakes;mistified;mistress;mistrust;mistrustful;mistrustfully;mists;misunderstand;misunderstanding;misunderstandings;misunderstood;misuse;moan;mobster;mock;mocked;mockeries;mockery;mocking;mockingly;mocks;molest;molestation;monotonous;monotony;monster;monstrosities;monstrosity;monstrous;monstrously;moody;moot;mope;morbid;morbidly;mordant;mordantly;moribund;moron;moronic;morons;mortification;mortified;mortify;mortifying;motionless;motley;mourn;mourner;mournful;mournfully;muddle;muddy;mudslinger;mudslinging;mulish;multi-polarization;mundane;murder;murderer;murderous;murderously;murky;muscle-flexing;mushy;musty;mysterious;mysteriously;mystery;mystify;myth;nag;nagging;naive;naively;narrower;nastily;nastiness;nasty;naughty;nauseate;nauseates;nauseating;nauseatingly;naïve;nebulous;nebulously;needless;needlessly;needy;nefarious;nefariously;negate;negation;negative;negatives;negativity;neglect;neglected;negligence;negligent;nemesis;nepotism;nervous;nervously;nervousness;nettle;nettlesome;neurotic;neurotically;niggle;niggles;nightmare;nightmarish;nightmarishly;nitpick;nitpicking;noise;noises;noisier;noisy;non-confidence;nonexistent;nonresponsive;nonsense;nosey;notoriety;notorious;notoriously;noxious;nuisance;numb;obese;object;objection;objectionable;objections;oblique;obliterate;obliterated;oblivious;obnoxious;obnoxiously;obscene;obscenely;obscenity;obscure;obscured;obscures;obscurity;obsess;obsessive;obsessively;obsessiveness;obsolete;obstacle;obstinate;obstinately;obstruct;obstructed;obstructing;obstruction;obstructs;obtrusive;obtuse;occlude;occluded;occludes;occluding;odd;odder;oddest;oddities;oddity;oddly;odor;offence;offend;offender;offending;offenses;offensive;offensively;offensiveness;officious;ominous;ominously;omission;omit;one-sided;onerous;onerously;onslaught;opinionated;opponent;opportunistic;oppose;opposition;oppositions;oppress;oppression;oppressive;oppressively;oppressiveness;oppressors;ordeal;orphan;ostracize;outbreak;outburst;outbursts;outcast;outcry;outlaw;outmoded;outrage;outraged;outrageous;outrageously;outrageousness;outrages;outsider;over-acted;over-awe;over-balanced;over-hyped;over-priced;over-valuation;overact;overacted;overawe;overbalance;overbalanced;overbearing;overbearingly;overblown;overdo;overdone;overdue;overemphasize;overheat;overkill;overloaded;overlook;overpaid;overpayed;overplay;overpower;overpriced;overrated;overreach;overrun;overshadow;oversight;oversights;oversimplification;oversimplified;oversimplify;oversize;overstate;overstated;overstatement;overstatements;overstates;overtaxed;overthrow;overthrows;overturn;overweight;overwhelm;overwhelmed;overwhelming;overwhelmingly;overwhelms;overzealous;overzealously;overzelous;pain;painful;painfull;painfully;pains;pale;pales;paltry;pan;pandemonium;pander;pandering;panders;panic;panick;panicked;panicking;panicky;paradoxical;paradoxically;paralize;paralyzed;paranoia;paranoid;parasite;pariah;parody;partiality;partisan;partisans;passe;passive;passiveness;pathetic;pathetically;patronize;paucity;pauper;paupers;payback;peculiar;peculiarly;pedantic;peeled;peeve;peeved;peevish;peevishly;penalize;penalty;perfidious;perfidity;perfunctory;peril;perilous;perilously;perish;pernicious;perplex;perplexed;perplexing;perplexity;persecute;persecution;pertinacious;pertinaciously;pertinacity;perturb;perturbed;pervasive;perverse;perversely;perversion;perversity;pervert;perverted;perverts;pessimism;pessimistic;pessimistically;pest;pestilent;petrified;petrify;pettifog;petty;phobia;phobic;phony;picket;picketed;picketing;pickets;picky;pig;pigs;pillage;pillory;pimple;pinch;pique;pitiable;pitiful;pitifully;pitiless;pitilessly;pittance;pity;plagiarize;plague;plasticky;plaything;plea;pleas;plebeian;plight;plot;plotters;ploy;plunder;plunderer;pointless;pointlessly;poison;poisonous;poisonously;pokey;poky;polarisation;polemize;pollute;polluter;polluters;polution;pompous;poor;poorer;poorest;poorly;posturing;pout;poverty;powerless;prate;pratfall;prattle;precarious;precariously;precipitate;precipitous;predatory;predicament;prejudge;prejudice;prejudices;prejudicial;premeditated;preoccupy;preposterous;preposterously;presumptuous;presumptuously;pretence;pretend;pretense;pretentious;pretentiously;prevaricate;pricey;pricier;prick;prickle;prickles;prideful;prik;primitive;prison;prisoner;problem;problematic;problems;procrastinate;procrastinates;procrastination;profane;profanity;prohibit;prohibitive;prohibitively;propaganda;propagandize;proprietary;prosecute;protest;protested;protesting;protests;protracted;provocation;provocative;provoke;pry;pugnacious;pugnaciously;pugnacity;punch;punish;punishable;punitive;punk;puny;puppet;puppets;puzzled;puzzlement;puzzling;quack;qualm;qualms;quandary;quarrel;quarrellous;quarrellously;quarrels;quarrelsome;quash;queer;questionable;quibble;quibbles;quitter;rabid;racism;racist;racists;racy;radical;radicalization;radically;radicals;rage;ragged;raging;rail;raked;rampage;rampant;ramshackle;rancor;randomly;rankle;rant;ranted;ranting;rantingly;rants;rape;raped;raping;rascal;rascals;rash;rattle;rattled;rattles;ravage;raving;reactionary;rebellious;rebuff;rebuke;recalcitrant;recant;recession;recessionary;reckless;recklessly;recklessness;recoil;recourses;redundancy;redundant;refusal;refuse;refused;refuses;refusing;refutation;refute;refuted;refutes;refuting;regress;regression;regressive;regret;regreted;regretful;regretfully;regrets;regrettable;regrettably;regretted;reject;rejected;rejecting;rejection;rejects;relapse;relentless;relentlessly;relentlessness;reluctance;reluctant;reluctantly;remorse;remorseful;remorsefully;remorseless;remorselessly;remorselessness;renounce;renunciation;repel;repetitive;reprehensible;reprehensibly;reprehension;reprehensive;repress;repression;repressive;reprimand;reproach;reproachful;reprove;reprovingly;repudiate;repudiation;repugn;repugnance;repugnant;repugnantly;repulse;repulsed;repulsing;repulsive;repulsively;repulsiveness;resent;resentful;resentment;resignation;resigned;restless;restlessness;restrict;restricted;restriction;restrictive;resurgent;retaliate;retaliatory;retard;retarded;retardedness;retards;reticent;retract;retreat;retreated;revenge;revengeful;revengefully;revert;revile;reviled;revoke;revolt;revolting;revoltingly;revulsion;revulsive;rhapsodize;rhetoric;rhetorical;ricer;ridicule;ridicules;ridiculous;ridiculously;rife;rift;rifts;rigid;rigidity;rigidness;rile;riled;rip;rip-off;ripoff;ripped;risk;risks;risky;rival;rivalry;roadblocks;rocky;rogue;rollercoaster;rot;rotten;rough;rremediable;rubbish;rude;rue;ruffian;ruffle;ruin;ruined;ruining;ruinous;ruins;rumbling;rumor;rumors;rumours;rumple;run-down;runaway;rupture;rust;rusts;rusty;rut;ruthless;ruthlessly;ruthlessness;ruts;sabotage;sack;sacrificed;sad;sadden;sadly;sadness;sag;sagged;sagging;saggy;sags;salacious;sanctimonious;sap;sarcasm;sarcastic;sarcastically;sardonic;sardonically;sass;satirical;satirize;savage;savaged;savagery;savages;scaly;scam;scams;scandal;scandalize;scandalized;scandalous;scandalously;scandals;scandel;scandels;scant;scapegoat;scar;scarce;scarcely;scarcity;scare;scared;scarier;scariest;scarily;scarred;scars;scary;scathing;scathingly;sceptical;scoff;scoffingly;scold;scolded;scolding;scoldingly;scorching;scorchingly;scorn;scornful;scornfully;scoundrel;scourge;scowl;scramble;scrambled;scrambles;scrambling;scrap;scratch;scratched;scratches;scratchy;scream;screech;screw-up;screwed;screwed-up;screwy;scuff;scuffs;scum;scummy;second-class;second-tier;secretive;sedentary;seedy;seethe;seething;self-coup;self-criticism;self-defeating;self-destructive;self-humiliation;self-interest;self-interested;self-serving;selfinterested;selfish;selfishly;selfishness;semi-retarded;senile;sensationalize;senseless;senselessly;seriousness;sermonize;servitude;set-up;setback;setbacks;sever;severe;severity;sh*t;shabby;shadowy;shady;shake;shaky;shallow;sham;shambles;shame;shameful;shamefully;shamefulness;shameless;shamelessly;shamelessness;shark;sharply;shatter;shemale;shimmer;shimmy;shipwreck;shirk;shirker;shit;shiver;shock;shocked;shocking;shockingly;shoddy;short-lived;shortage;shortchange;shortcoming;shortcomings;shortness;shortsighted;shortsightedness;showdown;shrew;shriek;shrill;shrilly;shrivel;shroud;shrouded;shrug;shun;shunned;sick;sicken;sickening;sickeningly;sickly;sickness;sidetrack;sidetracked;siege;sillily;silly;simplistic;simplistically;sin;sinful;sinfully;sinister;sinisterly;sink;sinking;skeletons;skeptic;skeptical;skeptically;skepticism;sketchy;skimpy;skinny;skittish;skittishly;skulk;slack;slander;slanderer;slanderous;slanderously;slanders;slap;slashing;slaughter;slaughtered;slave;slaves;sleazy;slime;slog;slogged;slogging;slogs;sloooooooooooooow;sloooow;slooow;sloow;sloppily;sloppy;sloth;slothful;slow;slow-moving;slowed;slower;slowest;slowly;sloww;slowww;slowwww;slug;sluggish;slump;slumping;slumpping;slur;slut;sluts;sly;smack;smallish;smash;smear;smell;smelled;smelling;smells;smelly;smelt;smoke;smokescreen;smolder;smoldering;smother;smoulder;smouldering;smudge;smudged;smudges;smudging;smug;smugly;smut;smuttier;smuttiest;smutty;snag;snagged;snagging;snags;snappish;snappishly;snare;snarky;snarl;sneak;sneakily;sneaky;sneer;sneering;sneeringly;snob;snobbish;snobby;snobish;snobs;snub;so-cal;soapy;sob;sober;sobering;solemn;solicitude;somber;sore;sorely;soreness;sorrow;sorrowful;sorrowfully;sorry;sour;sourly;spade;spank;spendy;spew;spewed;spewing;spews;spilling;spinster;spiritless;spite;spiteful;spitefully;spitefulness;splatter;split;splitting;spoil;spoilage;spoilages;spoiled;spoilled;spoils;spook;spookier;spookiest;spookily;spooky;spoon-fed;spoon-feed;spoonfed;sporadic;spotty;spurious;spurn;sputter;squabble;squabbling;squander;squash;squeak;squeaks;squeaky;squeal;squealing;squeals;squirm;stab;stagnant;stagnate;stagnation;staid;stain;stains;stale;stalemate;stall;stalls;stammer;stampede;standstill;stark;starkly;startle;startling;startlingly;starvation;starve;static;steal;stealing;steals;steep;steeply;stench;stereotype;stereotypical;stereotypically;stern;stew;sticky;stiff;stiffness;stifle;stifling;stiflingly;stigma;stigmatize;sting;stinging;stingingly;stingy;stink;stinks;stodgy;stole;stolen;stooge;stooges;stormy;straggle;straggler;strain;strained;straining;strange;strangely;stranger;strangest;strangle;streaky;strenuous;stress;stresses;stressful;stressfully;stricken;strict;strictly;strident;stridently;strife;strike;stringent;stringently;struck;struggle;struggled;struggles;struggling;strut;stubborn;stubbornly;stubbornness;stuck;stuffy;stumble;stumbled;stumbles;stump;stumped;stumps;stun;stunt;stunted;stupid;stupidest;stupidity;stupidly;stupified;stupify;stupor;stutter;stuttered;stuttering;stutters;sty;stymied;sub-par;subdued;subjected;subjection;subjugate;subjugation;submissive;subordinate;subpoena;subpoenas;subservience;subservient;substandard;subtract;subversion;subversive;subversively;subvert;succumb;suck;sucked;sucker;sucks;sucky;sue;sued;sueing;sues;suffer;suffered;sufferer;sufferers;suffering;suffers;suffocate;sugar-coat;sugar-coated;sugarcoated;suicidal;suicide;sulk;sullen;sully;sunder;sunk;sunken;superficial;superficiality;superficially;superfluous;superstition;superstitious;suppress;suppression;surrender;susceptible;suspect;suspicion;suspicions;suspicious;suspiciously;swagger;swamped;sweaty;swelled;swelling;swindle;swipe;swollen;symptom;symptoms;syndrome;taboo;tacky;taint;tainted;tamper;tangle;tangled;tangles;tank;tanked;tanks;tantrum;tardy;tarnish;tarnished;tarnishes;tarnishing;tattered;taunt;taunting;tauntingly;taunts;taut;tawdry;taxing;tease;teasingly;tedious;tediously;temerity;temper;tempest;temptation;tenderness;tense;tension;tentative;tentatively;tenuous;tenuously;tepid;terrible;terribleness;terribly;terror;terror-genic;terrorism;terrorize;testily;testy;tetchily;tetchy;thankless;thicker;thirst;thorny;thoughtless;thoughtlessly;thoughtlessness;thrash;threat;threaten;threatening;threats;threesome;throb;throbbed;throbbing;throbs;throttle;thug;thumb-down;thumbs-down;thwart;time-consuming;timid;timidity;timidly;timidness;tin-y;tingled;tingling;tired;tiresome;tiring;tiringly;toil;toll;top-heavy;topple;torment;tormented;torrent;tortuous;torture;tortured;tortures;torturing;torturous;torturously;totalitarian;touchy;toughness;tout;touted;touts;toxic;traduce;tragedy;tragic;tragically;traitor;traitorous;traitorously;tramp;trample;transgress;transgression;trap;traped;trapped;trash;trashed;trashy;trauma;traumatic;traumatically;traumatize;traumatized;travesties;travesty;treacherous;treacherously;treachery;treason;treasonous;trick;tricked;trickery;tricky;trivial;trivialize;trouble;troubled;troublemaker;troubles;troublesome;troublesomely;troubling;troublingly;truant;tumble;tumbled;tumbles;tumultuous;turbulent;turmoil;twist;twisted;twists;two-faced;two-faces;tyrannical;tyrannically;tyranny;tyrant;ugh;uglier;ugliest;ugliness;ugly;ulterior;ultimatum;ultimatums;ultra-hardline;un-viewable;unable;unacceptable;unacceptablely;unacceptably;unaccessible;unaccustomed;unachievable;unaffordable;unappealing;unattractive;unauthentic;unavailable;unavoidably;unbearable;unbearablely;unbelievable;unbelievably;uncaring;uncertain;uncivil;uncivilized;unclean;unclear;uncollectible;uncomfortable;uncomfortably;uncomfy;uncompetitive;uncompromising;uncompromisingly;unconfirmed;unconstitutional;uncontrolled;unconvincing;unconvincingly;uncooperative;uncouth;uncreative;undecided;undefined;undependability;undependable;undercut;undercuts;undercutting;underdog;underestimate;underlings;undermine;undermined;undermines;undermining;underpaid;underpowered;undersized;undesirable;undetermined;undid;undignified;undissolved;undocumented;undone;undue;unease;uneasily;uneasiness;uneasy;uneconomical;unemployed;unequal;unethical;uneven;uneventful;unexpected;unexpectedly;unexplained;unfairly;unfaithful;unfaithfully;unfamiliar;unfavorable;unfeeling;unfinished;unfit;unforeseen;unforgiving;unfortunate;unfortunately;unfounded;unfriendly;unfulfilled;unfunded;ungovernable;ungrateful;unhappily;unhappiness;unhappy;unhealthy;unhelpful;unilateralism;unimaginable;unimaginably;unimportant;uninformed;uninsured;unintelligible;unintelligile;unipolar;unjust;unjustifiable;unjustifiably;unjustified;unjustly;unkind;unkindly;unknown;unlamentable;unlamentably;unlawful;unlawfully;unlawfulness;unleash;unlicensed;unlikely;unlucky;unmoved;unnatural;unnaturally;unnecessary;unneeded;unnerve;unnerved;unnerving;unnervingly;unnoticed;unobserved;unorthodox;unorthodoxy;unpleasant;unpleasantries;unpopular;unpredictable;unprepared;unproductive;unprofitable;unprove;unproved;unproven;unproves;unproving;unqualified;unravel;unraveled;unreachable;unreadable;unrealistic;unreasonable;unreasonably;unrelenting;unrelentingly;unreliability;unreliable;unresolved;unresponsive;unrest;unruly;unsafe;unsatisfactory;unsavory;unscrupulous;unscrupulously;unsecure;unseemly;unsettle;unsettled;unsettling;unsettlingly;unskilled;unsophisticated;unsound;unspeakable;unspeakablely;unspecified;unstable;unsteadily;unsteadiness;unsteady;unsuccessful;unsuccessfully;unsupported;unsupportive;unsure;unsuspecting;unsustainable;untenable;untested;unthinkable;unthinkably;untimely;untouched;untrue;untrustworthy;untruthful;unusable;unusably;unuseable;unuseably;unusual;unusually;unviewable;unwanted;unwarranted;unwatchable;unwelcome;unwell;unwieldy;unwilling;unwillingly;unwillingness;unwise;unwisely;unworkable;unworthy;unyielding;upbraid;upheaval;uprising;uproar;uproarious;uproariously;uproarous;uproarously;uproot;upset;upseting;upsets;upsetting;upsettingly;urgent;useless;usurp;usurper;utterly;vagrant;vague;vagueness;vain;vainly;vanity;vehement;vehemently;vengeance;vengeful;vengefully;vengefulness;venom;venomous;venomously;vent;vestiges;vex;vexation;vexing;vexingly;vibrate;vibrated;vibrates;vibrating;vibration;vice;vicious;viciously;viciousness;victimize;vile;vileness;vilify;villainous;villainously;villains;villian;villianous;villianously;villify;vindictive;vindictively;vindictiveness;violate;violation;violator;violators;violent;violently;viper;virulence;virulent;virulently;virus;vociferous;vociferously;volatile;volatility;vomit;vomited;vomiting;vomits;vulgar;vulnerable;wack;wail;wallow;wane;waning;wanton;war-like;warily;wariness;warlike;warned;warning;warp;warped;wary;washed-out;waste;wasted;wasteful;wastefulness;wasting;water-down;watered-down;wayward;weak;weaken;weakening;weaker;weakness;weaknesses;weariness;wearisome;weary;wedge;weed;weep;weird;weirdly;wheedle;whimper;whine;whining;whiny;whips;whore;whores;wicked;wickedly;wickedness;wild;wildly;wiles;wilt;wily;wimpy;wince;wobble;wobbled;wobbles;woe;woebegone;woeful;woefully;womanizer;womanizing;worn;worried;worriedly;worrier;worries;worrisome;worry;worrying;worryingly;worse;worsen;worsening;worst;worthless;worthlessly;worthlessness;wound;wounds;wrangle;wrath;wreak;wreaked;wreaks;wreck;wrest;wrestle;wretch;wretched;wretchedly;wretchedness;wrinkle;wrinkled;wrinkles;wrip;wripped;wripping;writhe;wrong;wrongful;wrongly;wrought;yawn;zap;zapped;zaps;zealot;zealous;zealously;zombie;";
    //The list of positive words
    public static String POSITIVE_WORDS = ";a+;abound;abounds;abundance;abundant;accessable;accessible;acclaim;acclaimed;acclamation;accolad;accolades;accommodative;accomodative;accomplish;accomplished;accomplishment;accomplishments;accurate;accurately;achievable;achievement;achievements;achievible;acumen;adaptable;adaptive;adequate;adjustable;admirable;admirably;admiration;admire;admirer;admiring;admiringly;adorable;adore;adored;adorer;adoring;adoringly;adroit;adroitly;adulate;adulation;adulatory;advanced;advantage;advantageous;advantageously;advantages;adventuresome;adventurous;advocate;advocated;advocates;affability;affable;affably;affectation;affection;affectionate;affinity;affirm;affirmation;affirmative;affluence;affluent;afford;affordable;affordably;afordable;agile;agilely;agility;agreeable;agreeableness;agreeably;all-around;alluring;alluringly;altruistic;altruistically;amaze;amazed;amazement;amazes;amazing;amazingly;ambitious;ambitiously;ameliorate;amenable;amenity;amiability;amiabily;amiable;amicability;amicable;amicably;amity;ample;amply;amuse;amusing;amusingly;angel;angelic;apotheosis;appeal;appealing;applaud;appreciable;appreciate;appreciated;appreciates;appreciative;appreciatively;appropriate;approval;approve;ardent;ardently;ardor;articulate;aspiration;aspirations;aspire;assurance;assurances;assure;assuredly;assuring;astonish;astonished;astonishing;astonishingly;astonishment;astound;astounded;astounding;astoundingly;astutely;attentive;attraction;attractive;attractively;attune;audible;audibly;auspicious;authentic;authoritative;autonomous;available;aver;avid;avidly;award;awarded;awards;awe;awed;awesome;awesomely;awesomeness;awestruck;awsome;backbone;balanced;bargain;beauteous;beautiful;beautifullly;beautifully;beautify;beauty;beckon;beckoned;beckoning;beckons;believable;believeable;beloved;benefactor;beneficent;beneficial;beneficially;beneficiary;benefit;benefits;benevolence;benevolent;benifits;best;best-known;best-performing;best-selling;better;better-known;better-than-expected;beutifully;blameless;bless;blessing;bliss;blissful;blissfully;blithe;blockbuster;bloom;blossom;bolster;bonny;bonus;bonuses;boom;booming;boost;boundless;bountiful;brainiest;brainy;brand-new;brave;bravery;bravo;breakthrough;breakthroughs;breathlessness;breathtaking;breathtakingly;breeze;bright;brighten;brighter;brightest;brilliance;brilliances;brilliant;brilliantly;brisk;brotherly;bullish;buoyant;cajole;calm;calming;calmness;capability;capable;capably;captivate;captivating;carefree;cashback;cashbacks;catchy;celebrate;celebrated;celebration;celebratory;champ;champion;charisma;charismatic;charitable;charm;charming;charmingly;chaste;cheaper;cheapest;cheer;cheerful;cheery;cherish;cherished;cherub;chic;chivalrous;chivalry;civility;civilize;clarity;classic;classy;clean;cleaner;cleanest;cleanliness;cleanly;clear;clear-cut;cleared;clearer;clearly;clears;clever;cleverly;cohere;coherence;coherent;cohesive;colorful;comely;comfort;comfortable;comfortably;comforting;comfy;commend;commendable;commendably;commitment;commodious;compact;compactly;compassion;compassionate;compatible;competitive;complement;complementary;complemented;complements;compliant;compliment;complimentary;comprehensive;conciliate;conciliatory;concise;confidence;confident;congenial;congratulate;congratulation;congratulations;congratulatory;conscientious;considerate;consistent;consistently;constructive;consummate;contentment;continuity;contrasty;contribution;convenience;convenient;conveniently;convience;convienient;convient;convincing;convincingly;cool;coolest;cooperative;cooperatively;cornerstone;correct;correctly;cost-effective;cost-saving;counter-attack;counter-attacks;courage;courageous;courageously;courageousness;courteous;courtly;covenant;cozy;creative;credence;credible;crisp;crisper;cure;cure-all;cushy;cute;cuteness;danke;danken;daring;daringly;darling;dashing;dauntless;dawn;dazzle;dazzled;dazzling;dead-cheap;dead-on;decency;decent;decisive;decisiveness;dedicated;deep;deeper;deepest;defeat;defeated;defeating;defeats;defender;deference;deft;deginified;delectable;delicacy;delicate;delicious;delight;delighted;delightful;delightfully;delightfulness;dependable;dependably;deservedly;deserving;desirable;desiring;desirous;destiny;detachable;devout;dexterous;dexterously;dextrous;dignified;dignify;dignity;diligence;diligent;diligently;diplomatic;dirt-cheap;distinction;distinctive;distinguished;diversified;divine;divinely;dominate;dominated;dominates;dote;dotingly;doubtless;dreamland;dumbfounded;dumbfounding;dummy-proof;durable;dynamic;eager;eagerly;eagerness;earnest;earnestly;earnestness;ease;eased;eases;easier;easiest;easiness;easing;easy;easy-to-use;easygoing;ebullience;ebullient;ebulliently;ecenomical;economical;ecstasies;ecstasy;ecstatic;ecstatically;edify;educated;effective;effectively;effectiveness;effectual;efficacious;efficient;efficiently;effortless;effortlessly;effusion;effusive;effusively;effusiveness;elan;elate;elated;elatedly;elation;electrify;elegance;elegant;elegantly;elevate;elite;eloquence;eloquent;eloquently;embolden;eminence;eminent;empathize;empathy;empower;empowerment;enchant;enchanted;enchanting;enchantingly;encourage;encouragement;encouraging;encouragingly;endear;endearing;endorse;endorsed;endorsement;endorses;endorsing;energetic;energize;energy-efficient;energy-saving;engaging;engrossing;enhance;enhanced;enhancement;enhances;enjoy;enjoyable;enjoyably;enjoyed;enjoying;enjoyment;enjoys;enlighten;enlightenment;enliven;ennoble;enough;enrapt;enrapture;enraptured;enrich;enrichment;enterprising;entertain;entertaining;entertains;enthral;enthrall;enthralled;enthuse;enthusiasm;enthusiast;enthusiastic;enthusiastically;entice;enticed;enticing;enticingly;entranced;entrancing;entrust;enviable;enviably;envious;enviously;enviousness;envy;equitable;ergonomical;err-free;erudite;ethical;eulogize;euphoria;euphoric;euphorically;evaluative;evenly;eventful;everlasting;evocative;exalt;exaltation;exalted;exaltedly;exalting;exaltingly;examplar;examplary;excallent;exceed;exceeded;exceeding;exceedingly;exceeds;excel;exceled;excelent;excellant;excelled;excellence;excellency;excellent;excellently;excels;exceptional;exceptionally;excite;excited;excitedly;excitedness;excitement;excites;exciting;excitingly;exellent;exemplar;exemplary;exhilarate;exhilarating;exhilaratingly;exhilaration;exonerate;expansive;expeditiously;expertly;exquisite;exquisitely;extol;extoll;extraordinarily;extraordinary;exuberance;exuberant;exuberantly;exult;exultant;exultation;exultingly;eye-catch;eye-catching;eyecatch;eyecatching;fabulous;fabulously;facilitate;fair;fairly;fairness;faith;faithful;faithfully;faithfulness;fame;famed;famous;famously;fancier;fancinating;fancy;fanfare;fans;fantastic;fantastically;fascinate;fascinating;fascinatingly;fascination;fashionable;fashionably;fast;fast-growing;fast-paced;faster;fastest;fastest-growing;faultless;fav;fave;favor;favorable;favored;favorite;favorited;favour;fearless;fearlessly;feasible;feasibly;feat;feature-rich;fecilitous;feisty;felicitate;felicitous;felicity;fertile;fervent;fervently;fervid;fervidly;fervor;festive;fidelity;fiery;fine;fine-looking;finely;finer;finest;firmer;first-class;first-in-class;first-rate;flashy;flatter;flattering;flatteringly;flawless;flawlessly;flexibility;flexible;flourish;flourishing;fluent;flutter;fond;fondly;fondness;foolproof;foremost;foresight;formidable;fortitude;fortuitous;fortuitously;fortunate;fortunately;fortune;fragrant;free;freed;freedom;freedoms;fresh;fresher;freshest;friendliness;friendly;frolic;frugal;fruitful;ftw;fulfillment;fun;futurestic;futuristic;gaiety;gaily;gain;gained;gainful;gainfully;gaining;gains;gallant;gallantly;galore;geekier;geeky;gem;gems;generosity;generous;generously;genial;genius;gentle;gentlest;genuine;gifted;glad;gladden;gladly;gladness;glamorous;glee;gleeful;gleefully;glimmer;glimmering;glisten;glistening;glitter;glitz;glorify;glorious;gloriously;glory;glow;glowing;glowingly;god-given;god-send;godlike;godsend;gold;golden;good;goodly;goodness;goodwill;goood;gooood;gorgeous;gorgeously;grace;graceful;gracefully;gracious;graciously;graciousness;grand;grandeur;grateful;gratefully;gratification;gratified;gratifies;gratify;gratifying;gratifyingly;gratitude;great;greatest;greatness;grin;groundbreaking;guarantee;guidance;guiltless;gumption;gush;gusto;gutsy;hail;halcyon;hale;hallmark;hallmarks;hallowed;handier;handily;hands-down;handsome;handsomely;handy;happier;happily;happiness;happy;hard-working;hardier;hardy;harmless;harmonious;harmoniously;harmonize;harmony;headway;heal;healthful;healthy;hearten;heartening;heartfelt;heartily;heartwarming;heaven;heavenly;helped;helpful;helping;hero;heroic;heroically;heroine;heroize;heros;high-quality;high-spirited;hilarious;holy;homage;honest;honesty;honor;honorable;honored;honoring;hooray;hopeful;hospitable;hotcake;hotcakes;hottest;hug;humane;humble;humility;humor;humorous;humorously;humour;humourous;ideal;idealize;ideally;idol;idolize;idolized;idyllic;illuminate;illuminati;illuminating;illumine;illustrious;ilu;imaculate;imaginative;immaculate;immaculately;immense;impartial;impartiality;impartially;impassioned;impeccable;impeccably;important;impress;impressed;impresses;impressive;impressively;impressiveness;improve;improved;improvement;improvements;improves;improving;incredible;incredibly;indebted;individualized;indulgence;indulgent;industrious;inestimable;inestimably;inexpensive;infallibility;infallible;infallibly;influential;ingenious;ingeniously;ingenuity;ingenuous;ingenuously;innocuous;innovation;innovative;inpressed;insightful;insightfully;inspiration;inspirational;inspire;inspiring;instantly;instructive;instrumental;integral;integrated;intelligence;intelligent;intelligible;interesting;interests;intimacy;intimate;intricate;intrigue;intriguing;intriguingly;intuitive;invaluable;invaluablely;inventive;invigorate;invigorating;invincibility;invincible;inviolable;inviolate;invulnerable;irreplaceable;irreproachable;irresistible;irresistibly;issue-free;jaw-droping;jaw-dropping;jollify;jolly;jovial;joy;joyful;joyfully;joyous;joyously;jubilant;jubilantly;jubilate;jubilation;jubiliant;judicious;justly;keen;keenly;keenness;kid-friendly;kindliness;kindly;kindness;knowledgeable;kudos;large-capacity;laud;laudable;laudably;lavish;lavishly;law-abiding;lawful;lawfully;lead;leading;leads;lean;led;legendary;leverage;levity;liberate;liberation;liberty;lifesaver;light-hearted;lighter;likable;like;liked;likes;liking;lionhearted;lively;logical;long-lasting;loud;louder;lovable;lovably;love;loved;loveliness;lovely;lover;loves;loving;low-cost;low-price;low-priced;low-risk;lower-priced;loyal;loyalty;lucid;lucidly;luck;luckier;luckiest;luckiness;lucky;lucrative;luminous;lush;luster;lustrous;luxuriant;luxuriate;luxuriouss;luxuriously;luxury;lyrical;magic;magical;magnanimous;magnanimously;magnificence;magnificent;magnificently;majestic;majesty;manageable;maneuverable;marvel;marveled;marvelled;marvellous;marvelous;marvelously;marvelousness;marvels;master;masterful;masterfully;masterpiece;masterpieces;masters;mastery;matchless;mature;maturely;maturity;meaningful;memorable;merciful;mercifully;mercy;merit;meritorious;merrily;merriment;merriness;merry;mesmerize;mesmerized;mesmerizes;mesmerizing;mesmerizingly;meticulous;meticulously;mightily;mighty;mind-blowing;miracle;miracles;miraculous;miraculously;miraculousness;modern;modest;modesty;momentous;monumental;monumentally;morality;motivated;multi-purpose;navigable;neat;neatest;neatly;nice;nicely;nicer;nicest;nifty;nimble;noble;nobly;noiseless;non-violence;non-violent;notably;noteworthy;nourish;nourishing;nourishment;novelty;nurturing;oasis;obsession;obsessions;obtainable;openly;openness;optimal;optimism;optimistic;opulent;orderly;originality;outdo;outdone;outperform;outperformed;outperforming;outperforms;outshine;outshone;outsmart;outstanding;outstandingly;outstrip;outwit;ovation;overjoyed;overtake;overtaken;overtakes;overtaking;overtook;overture;pain-free;painless;painlessly;palatial;pamper;pampered;pamperedly;pamperedness;pampers;panoramic;paradise;paramount;pardon;passion;passionate;passionately;patience;patient;patiently;patriot;patriotic;peace;peaceable;peaceful;peacefully;peacekeepers;peach;peerless;pep;pepped;pepping;peppy;peps;perfect;perfection;perfectly;permissible;perseverance;persevere;personages;personalized;phenomenal;phenomenally;picturesque;piety;pinnacle;playful;playfully;pleasant;pleasantly;pleased;pleases;pleasing;pleasingly;pleasurable;pleasurably;pleasure;plentiful;pluses;plush;plusses;poetic;poeticize;poignant;poise;poised;polished;polite;politeness;popular;portable;posh;positive;positively;positives;powerful;powerfully;praise;praiseworthy;praising;pre-eminent;precious;precise;precisely;preeminent;prefer;preferable;preferably;prefered;preferes;preferring;prefers;premier;prestige;prestigious;prettily;pretty;priceless;pride;principled;privilege;privileged;prize;proactive;problem-free;problem-solver;prodigious;prodigiously;prodigy;productive;productively;proficient;proficiently;profound;profoundly;profuse;profusion;progress;progressive;prolific;prominence;prominent;promise;promised;promises;promising;promoter;prompt;promptly;proper;properly;propitious;propitiously;pros;prosper;prosperity;prosperous;prospros;protect;protection;protective;proud;proven;proves;providence;proving;prowess;prudence;prudent;prudently;punctual;pure;purify;purposeful;quaint;qualified;qualify;quicker;quiet;quieter;radiance;radiant;rapid;rapport;rapt;rapture;raptureous;raptureously;rapturous;rapturously;rational;razor-sharp;reachable;readable;readily;ready;reaffirm;reaffirmation;realistic;realizable;reasonable;reasonably;reasoned;reassurance;reassure;receptive;reclaim;recomend;recommend;recommendation;recommendations;recommended;reconcile;reconciliation;record-setting;recover;recovery;rectification;rectify;rectifying;redeem;redeeming;redemption;refine;refined;refinement;reform;reformed;reforming;reforms;refresh;refreshed;refreshing;refund;refunded;regal;regally;regard;rejoice;rejoicing;rejoicingly;rejuvenate;rejuvenated;rejuvenating;relaxed;relent;reliable;reliably;relief;relish;remarkable;remarkably;remedy;remission;remunerate;renaissance;renewed;renown;renowned;replaceable;reputable;reputation;resilient;resistance;resolute;resound;resounding;resourceful;resourcefulness;respect;respectable;respectful;respectfully;respite;resplendent;responsibly;responsive;restful;restored;restructure;restructured;restructuring;retractable;revel;revelation;revere;reverence;reverent;reverently;revitalize;revival;revive;revives;revolutionary;revolutionize;revolutionized;revolutionizes;reward;rewarding;rewardingly;rich;richer;richly;richness;righten;righteous;righteously;righteousness;rightful;rightfully;rightly;rightness;risk-free;robust;rock-star;rock-stars;rockstar;rockstars;romantic;romantically;romanticize;roomier;roomy;rosy;safe;safely;sagacity;sagely;saint;saintliness;saintly;salutary;salute;sane;satisfactorily;satisfactory;satisfied;satisfies;satisfy;satisfying;satisified;saturated;saver;savings;savior;savvy;scenic;seamless;seasoned;secure;securely;selective;self-determination;self-respect;self-satisfaction;self-sufficiency;self-sufficient;sensation;sensational;sensationally;sensations;sensible;sensibly;sensitive;serene;serenity;sexy;sharp;sharper;sharpest;shimmering;shimmeringly;shine;shiny;significant;silent;simpler;simplest;simplified;simplifies;simplify;simplifying;sincere;sincerely;sincerity;skill;skilled;skillful;skillfully;slammin;sleek;slick;slim;slimmer;slimmest;smart;smarter;smartest;smartly;smile;smiles;smiling;smilingly;smitten;smooth;smoother;smoothes;smoothest;smoothly;snappy;snazzy;sociable;soft;softer;solace;solicitous;solicitously;solid;solidarity;soothe;soothingly;sophisticated;soulful;soundly;soundness;spacious;sparkle;sparkling;spectacular;spectacularly;speedily;speedy;spellbind;spellbinding;spellbindingly;spellbound;spirited;spiritual;splendid;splendidly;splendor;spontaneous;sporty;spotless;sprightly;stability;stabilize;stable;stainless;standout;state-of-the-art;stately;statuesque;staunch;staunchly;staunchness;steadfast;steadfastly;steadfastness;steadiest;steadiness;steady;stellar;stellarly;stimulate;stimulates;stimulating;stimulative;stirringly;straighten;straightforward;streamlined;striking;strikingly;striving;strong;stronger;strongest;stunned;stunning;stunningly;stupendous;stupendously;sturdier;sturdy;stylish;stylishly;stylized;suave;suavely;sublime;subsidize;subsidized;subsidizes;subsidizing;substantive;succeed;succeeded;succeeding;succeeds;succes;success;successes;successful;successfully;suffice;sufficed;suffices;sufficient;sufficiently;suitable;sumptuous;sumptuously;sumptuousness;super;superb;superbly;superior;superiority;supple;support;supported;supporter;supporting;supportive;supports;supremacy;supreme;supremely;supurb;supurbly;surmount;surpass;surreal;survival;survivor;sustainability;sustainable;swank;swankier;swankiest;swanky;sweeping;sweet;sweeten;sweetheart;sweetly;sweetness;swift;swiftness;talent;talented;talents;tantalize;tantalizing;tantalizingly;tempt;tempting;temptingly;tenacious;tenaciously;tenacity;tender;tenderly;terrific;terrifically;thank;thankful;thinner;thoughtful;thoughtfully;thoughtfulness;thrift;thrifty;thrill;thrilled;thrilling;thrillingly;thrills;thrive;thriving;thumb-up;thumbs-up;tickle;tidy;time-honored;timely;tingle;titillate;titillating;titillatingly;togetherness;tolerable;toll-free;top;top-notch;top-quality;topnotch;tops;tough;tougher;toughest;traction;tranquil;tranquility;transparent;treasure;tremendously;trendy;triumph;triumphal;triumphant;triumphantly;trivially;trophy;trouble-free;trump;trumpet;trust;trusted;trusting;trustingly;trustworthiness;trustworthy;trusty;truthful;truthfully;truthfulness;twinkly;ultra-crisp;unabashed;unabashedly;unaffected;unassailable;unbeatable;unbiased;unbound;uncomplicated;unconditional;undamaged;undaunted;understandable;undisputable;undisputably;undisputed;unencumbered;unequivocal;unequivocally;unfazed;unfettered;unforgettable;unity;unlimited;unmatched;unparalleled;unquestionable;unquestionably;unreal;unrestricted;unrivaled;unselfish;unwavering;upbeat;upgradable;upgradeable;upgraded;upheld;uphold;uplift;uplifting;upliftingly;upliftment;upscale;usable;useable;useful;user-friendly;user-replaceable;valiant;valiantly;valor;valuable;variety;venerate;verifiable;veritable;versatile;versatility;vibrant;vibrantly;victorious;victory;viewable;vigilance;vigilant;virtue;virtuous;virtuously;visionary;vivacious;vivid;vouch;vouchsafe;warm;warmer;warmhearted;warmly;warmth;wealthy;welcome;well;well-backlit;well-balanced;well-behaved;well-being;well-bred;well-connected;well-educated;well-established;well-informed;well-intentioned;well-known;well-made;well-managed;well-mannered;well-positioned;well-received;well-regarded;well-rounded;well-run;well-wishers;wellbeing;whoa;wholeheartedly;wholesome;whooa;whoooa;wieldy;willing;willingly;willingness;win;windfall;winnable;winner;winners;winning;wins;wisdom;wise;wisely;witty;won;wonder;wonderful;wonderfully;wonderous;wonderously;wonders;wondrous;woo;work;workable;worked;works;world-famous;worth;worth-while;worthiness;worthwhile;worthy;wow;wowed;wowing;wows;yay;youthful;zeal;zenith;zest;zippy;";
    //The list of extra opinion words
    private static String EXTRA_WORDS = ";small;smallest;smaller;lightweight;big;bigger;biggest;";

    public static void loadSDict() throws IOException {
        sDict = FileUtils.readFileToString(new File(".\\sDict.txt"));
    }

    public static boolean findAgreement(NounPhrase np1) {
        //true: singular
        //false: plural
        boolean numberNP1 = false;
        if ((np1.getHeadLabel().equals("NN")
                || np1.getHeadLabel().equals("NNP")
                || SINGULAR_KEYWORDS.contains(";" + np1.getHeadNode().value().toLowerCase() + ";"))
                || np1.getHeadLabel().equals("JJ")
                || np1.getHeadLabel().equals("JJS")
                || np1.getHeadLabel().equals("JJR")
                || np1.getHeadLabel().equals("CD")
                || np1.getHeadLabel().equals("WP")
                || np1.getHeadLabel().equals("WDT")
                || np1.getHeadLabel().equals("MD")
                || np1.getHeadLabel().equals("RB")
                || np1.getHeadLabel().equals("RBR")
                || np1.getHeadLabel().equals("IN")
                || np1.getHeadLabel().equals("VBG")) {
            numberNP1 = true;
        }
        return numberNP1;
    }

    /**
     * Check if two Noun Phrases agree in NUMBER
     *
     * @param np1
     * @param np2
     * @return true if agree, otherwise false
     */
    public static boolean numberAgreementExtract(NounPhrase np1, NounPhrase np2) {
        if (findAgreement(np1) == findAgreement(np2)) {
            return true;
        }
        return false;
    }

    /**
     * Check for is-between feature
     *
     * @param review np1 np2
     * @param np1
     * @param np2
     * @return true if there is an 'is-like' between 2 NPs, otherwise false
     */
    public static Boolean isBetweenExtract(Review review, NounPhrase np1, NounPhrase np2) {
        if (np1.getReviewId() == np2.getReviewId()) {
            if (np1.getSentenceId() == np2.getSentenceId()) {
                Sentence curSentence = review.getSentences().get(np1.getSentenceId());
                if (np1.getOffsetEnd() < np2.getOffsetBegin()) {
                    if (np1.getOffsetEnd() + 1 < np2.getOffsetBegin() && contains3rdTobe(curSentence.getRawContent().substring(np1.getOffsetEnd() + 1 - curSentence.getOffsetBegin(), np2.getOffsetBegin() - curSentence.getOffsetBegin()))) {
                        if (findComparativeIndicator(curSentence, np1, np2).isEmpty()) {
                            return true;
                        }
                    }
                } else if (np2.getOffsetEnd() < np1.getOffsetBegin()) {
                    if (np2.getOffsetEnd() + 1 < np1.getOffsetBegin() && contains3rdTobe(curSentence.getRawContent().substring(np2.getOffsetEnd() + 1 - curSentence.getOffsetBegin(), np1.getOffsetBegin() - curSentence.getOffsetBegin()))) {
                        if (findComparativeIndicator(curSentence, np1, np2).isEmpty()) {
                            return true;
                        }
                    }
                }
            }
        }

        return false;
    }

    /**
     * Check for is-between feature
     *
     * @param review np1 np2
     * @param np1
     * @param np2
     * @return true if there is an 'is-like' between 2 NPs, otherwise false
     */
    public static Boolean isBetween2Extract(Review review, NounPhrase np1, NounPhrase np2) {
        if (np1.getReviewId() == np2.getReviewId()) {
            if (np1.getSentenceId() == np2.getSentenceId()) {
                Sentence curSentence = review.getSentences().get(np1.getSentenceId());
                if (np1.getOffsetEnd() < np2.getOffsetBegin()) {
                    if (np1.getOffsetEnd() + 1 < np2.getOffsetBegin() && contains3rdTobe(curSentence.getRawContent().substring(np1.getOffsetEnd() + 1 - curSentence.getOffsetBegin(), np2.getOffsetBegin() - curSentence.getOffsetBegin()))) {
                        if (findComparativeIndicator(curSentence, np1, np2).isEmpty() && !hasNpBetween(np1, np2) && !hasVPBetween(np1, np2)) {
                            return true;
                        }
                    }
                } else if (np2.getOffsetEnd() < np1.getOffsetBegin()) {
                    if (np2.getOffsetEnd() + 1 < np1.getOffsetBegin() && contains3rdTobe(curSentence.getRawContent().substring(np2.getOffsetEnd() + 1 - curSentence.getOffsetBegin(), np1.getOffsetBegin() - curSentence.getOffsetBegin()))) {
                        if (findComparativeIndicator(curSentence, np1, np2).isEmpty() && !hasNpBetween(np1, np2) && !hasVPBetween(np1, np2)) {
                            return true;
                        }
                    }
                }
            }
        }

        return false;
    }

    /**
     * Check for is-between feature
     *
     * @param review np1 np2
     * @param np1
     * @param np2
     * @return true if there is an 'is-like' between 2 NPs, otherwise false
     */
    public static Boolean isBetween3Extract(Review review, NounPhrase np1, NounPhrase np2) {
        if (np1.getReviewId() == np2.getReviewId()) {
            if (np1.getSentenceId() == np2.getSentenceId()) {
                Sentence curSentence = review.getSentences().get(np1.getSentenceId());
                if (np1.getOffsetEnd() < np2.getOffsetBegin()) {
                    if (np1.getOffsetEnd() + 1 < np2.getOffsetBegin() && contains3rdTobe(curSentence.getRawContent().substring(np1.getOffsetEnd() + 1 - curSentence.getOffsetBegin(), np2.getOffsetBegin() - curSentence.getOffsetBegin()))) {
                        if (!hasNpBetween(np1, np2) && (np2.getOffsetBegin() - np1.getOffsetEnd()) < 7) {
                            return true;
                        }
                    }
                } else if (np2.getOffsetEnd() < np1.getOffsetBegin()) {
                    if (np2.getOffsetEnd() + 1 < np1.getOffsetBegin() && contains3rdTobe(curSentence.getRawContent().substring(np2.getOffsetEnd() + 1 - curSentence.getOffsetBegin(), np1.getOffsetBegin() - curSentence.getOffsetBegin()))) {
                        if (findComparativeIndicator(curSentence, np1, np2).isEmpty() && !hasNpBetween(np1, np2)) {
                            return true;
                        }
                    }
                }
            }
        }

        return false;
    }

    /**
     * Check for comparativeIndicator-between feature
     *
     * @param review
     * @param np1
     * @param np2
     * @return true if there is a comparative indicator between, otherwise false
     */
    public static Boolean comparativeIndicatorExtract(Review review, NounPhrase np1, NounPhrase np2) {
        if (np1.getReviewId() == np2.getReviewId()) {
            if (np1.getSentenceId() == np2.getSentenceId()) {
                Sentence curSentence = review.getSentences().get(np1.getSentenceId());
                if (np1.getOffsetEnd() < np2.getOffsetBegin()) {
                    if (!findComparativeIndicator(curSentence, np1, np2).isEmpty()) {
                        return true;
                    }
                } else if (np2.getOffsetEnd() < np1.getOffsetBegin()) {
                    if (!findComparativeIndicator(curSentence, np2, np1).isEmpty()) {
                        return true;
                    }
                }
            }
        }

        return false;
    }

    /**
     * Check for comparativeIndicator-between feature
     *
     * @param review
     * @param np1
     * @param np2
     * @return true if there is a comparative indicator between, otherwise false
     */
    public static Boolean comparativeIndicator2Extract(Review review, NounPhrase np1, NounPhrase np2) {
        if (np1.getReviewId() == np2.getReviewId()) {
            if (np1.getSentenceId() == np2.getSentenceId()) {
                Sentence curSentence = review.getSentences().get(np1.getSentenceId());
                if (np1.getOffsetEnd() < np2.getOffsetBegin()) {
                    if (!findComparativeIndicator(curSentence, np1, np2).isEmpty() && !hasNpBetween(np1, np2)) {
                        return true;
                    }
                } else if (np2.getOffsetEnd() < np1.getOffsetBegin()) {
                    if (!findComparativeIndicator(curSentence, np2, np1).isEmpty() && !hasNpBetween(np1, np2)) {
                        return true;
                    }
                }
            }
        }

        return false;
    }

    /**
     * Get comparative indicator phrase in a sentence or between 2 NPs in a
     * sentence
     *
     * @param curSentence
     * @param np1
     * @param np2
     * @return
     */
    public static List<Token> findComparativeIndicator(Sentence curSentence, NounPhrase np1, NounPhrase np2) {
        List<Token> res = new ArrayList<>();

        int offsetTraverseBegin = np1 != null ? np1.getOffsetEnd() : curSentence.getOffsetBegin();
        int offsetTraverseEnd = np2 != null ? np2.getOffsetBegin() : curSentence.getOffsetEnd();

        int i = 0;
        List<Token> tokens = curSentence.getTokens();
        while (i < tokens.size()) {
            Token token = tokens.get(i);
            if (token.getOffsetBegin() > offsetTraverseBegin && token.getOffsetEnd() < offsetTraverseEnd) {

                //Case 0: beat, win, outperform
                if (!isNoun(token) && isComparativeVerb(token)) {
                    res.add(token);
                } //Case 1: comparative ADJ + than (including "more than", "less than", ...)
                else if ((!token.getWord().toLowerCase().equals("more") || !token.getWord().toLowerCase().equals("less"))
                        && (isComparativeAdjective(token) || isComparativeAdverb(token))) {
                    if (tokens.get(i + 1).getWord().equals("than")) {
                        res.add(token);
                        res.add(tokens.get(i + 1));
                        ++i;
                    }
                    if (isNoun(tokens.get(i + 1))) {
                        if (isNoun(tokens.get(i + 2))) {
                            if (tokens.get(i + 3).getWord().equals("than")) {
                                res.add(token);
                                res.add(tokens.get(i + 1));
                                res.add(tokens.get(i + 2));
                                res.add(tokens.get(i + 3));
                                i += 3;
                            }
                        } else if (tokens.get(i + 2).getWord().equals("than")) {
                            res.add(token);
                            res.add(tokens.get(i + 1));
                            res.add(tokens.get(i + 2));
                            i += 2;
                        }
                    }
                } else if (isAdjective(token) || isAdverb(token)) {
                    //more/less + adj/adv + N + than
                    if (isNoun(tokens.get(i + 1))) {
                        if (isNoun(tokens.get(i + 2))) {
                            if (tokens.get(i + 3).getWord().equals("than")) {
                                if (tokens.get(i - 1).getWord().equals("more")
                                        || tokens.get(i - 1).getWord().equals("less")) {
                                    res.add(tokens.get(i - 1));
                                    res.add(token);
                                    res.add(tokens.get(i + 1));
                                    res.add(tokens.get(i + 2));
                                    res.add(tokens.get(i + 3));
                                    i += 3;
                                }
                            }
                        } else if (tokens.get(i + 2).getWord().equals("than")) {
                            if (tokens.get(i - 1).getWord().equals("more")
                                    || tokens.get(i - 1).getWord().equals("less")) {
                                res.add(tokens.get(i - 1));
                                res.add(token);
                                res.add(tokens.get(i + 1));
                                res.add(tokens.get(i + 2));
                                i += 2;
                            }
                        }
                    }

                    if (tokens.get(i + 1).getWord().equals("than")) {
                        //Case 2: more/less + ADJ/ADV + than
                        if (tokens.get(i - 1).getWord().equals("more")
                                || tokens.get(i - 1).getWord().equals("less")) {
                            res.add(tokens.get(i - 1));
                            res.add(token);
                            res.add(tokens.get(i + 1));
                            ++i;
                        }
                    }

                    if (np1 != null && np2 != null && tokens.get(i + 1).getWord().equals("as")) {
                        //Case 4: as + ADJ/ADV + as
                        if (tokens.get(i - 1).getWord().equals("as")) {
                            res.add(tokens.get(i - 1));
                            res.add(token);
                            res.add(tokens.get(i + 1));
                            ++i;
                        }
                    }
                } //Case 5: special comparative adj + to
                else if (isSpecialComparativeAdjective(token)) {
                    if (tokens.get(i + 1).getWord().equals("to")) {
                        res.add(token);
                        res.add(tokens.get(i + 1));
                        ++i;
                    }
                } //Case 6: same + N + as
                else if (np1 != null && np2 != null && isNoun(token)) {
                    if (tokens.get(i - 1).getWord().equals("same")) {
                        if (tokens.get(i + 1).getWord().equals("as")) {
                            res.add(tokens.get(i - 1));
                            res.add(token);
                            res.add(tokens.get(i + 1));
                            ++i;
                        }
                    }
                } else if (token.getWord().equals("than")) {
                    if (!tokens.get(i - 1).getWord().toLowerCase().equals("other")){
                        res.add(token);
                    }                    
                }
            }
            ++i;
        }

        return res;
    }

    public static Boolean isCorefTest(NounPhrase np1, NounPhrase np2) {
        if (np1.getType() == 1 || np2.getType() == 1) {
            return false;
        }
        if (np1.getRefId() == np2.getId() || np2.getRefId() == np1.getId()) {
            return true;
        } else if (np1.getRefId() == np2.getRefId() && np1.getRefId() != -1) {
            return true;
        } else {
            return false;
        }
    }

    private static boolean contains3rdTobe(String sequence) {
        for (String aTobeVerb : TO_BES) {
            if (sequence.contains(aTobeVerb) && (sequence.contains("is not") || sequence.contains("isn't"))) {
                return true;
            }
        }
        return false;
    }

    private static boolean hasNpBetween(NounPhrase np1, NounPhrase np2) {
        int offsetBeginRange = np1.getOffsetBegin() < np2.getOffsetBegin() ? np1.getOffsetEnd() : np2.getOffsetEnd();
        int offsetEndRange = np1.getOffsetBegin() < np2.getOffsetBegin() ? np2.getOffsetBegin() : np1.getOffsetBegin();
        List<NounPhrase> nounPhrases = StanfordUtil.reviews.get(np1.getReviewId()).getSentences().get(np1.getSentenceId()).getNounPhrases();
        for (NounPhrase np : nounPhrases) {
            if (np.getOffsetBegin() > offsetBeginRange && np.getOffsetEnd() < offsetEndRange) {
                return true;
            }
            // np1 is considered before np2
            if ((np.getOffsetBegin() == np2.getOffsetBegin())
                    && (np.getNpNode().getLeaves().size() > np2.getNpNode().getLeaves().size())) {
                return true;
            }
        }

        return false;
    }
    
    //check if exist the Verb between 2 NPs
    private static boolean hasVPBetween(NounPhrase np1, NounPhrase np2){
    	int offsetBeginRange = np1.getOffsetBegin() < np2.getOffsetBegin() ? np1.getOffsetEnd() : np2.getOffsetEnd();
        int offsetEndRange = np1.getOffsetBegin() < np2.getOffsetBegin() ? np2.getOffsetBegin() : np1.getOffsetBegin();
        Sentence se = StanfordUtil.reviews.get(np1.getReviewId()).getSentences().get(np1.getSentenceId());
        for (Token tk: se.getTokens())
        	if (tk.getOffsetBegin() > offsetBeginRange && tk.getOffsetEnd() < offsetEndRange){
        		if (tk.getPOS().contains("VB") && !listTOBE.contains(tk.getWord().toLowerCase()))
        			return true;
        	}
        return false;
    }

    private static boolean isNoun(Token token) {
        return (token.getPOS().equals(SINGULAR_NOUN_TAG) || token.getPOS().equals(PLURAL_NOUN_TAG));
    }

    private static boolean isAdjective(Token token) {
        return (token.getPOS().equals(ADJECTIVE_TAG));
    }

    private static boolean isComparativeAdjective(Token token) {
        return (token.getPOS().equals(COMPARATIVE_ADJECTIVE_TAG));
    }

    private static boolean isSpecialComparativeAdjective(Token token) {
        for (String aSpecialComparative : SPECIAL_COMPARATIVES) {
            if (token.getWord().equals(aSpecialComparative)) {
                return true;
            }
        }
        return false;
    }

    private static boolean isComparativeAdverb(Token token) {
        return (token.getPOS().equals(COMPARATIVE_ADVERB_TAG));
    }

    private static boolean isAdverb(Token token) {
        return (token.getPOS().equals(ADVERB_TAG));
    }

    private static boolean isComparativeVerb(Token token) {
        return COMPARATIVE_VERBS.contains(";" + token.getWord() + ";");
    }

    public static Boolean isPronoun(NounPhrase np) {
        if (np.getNpNode().numChildren() == 1) {
            return PRONOUNS.contains(np.getNpNode().getLeaves().get(0).toString().toLowerCase());
        } else {
            return false;
        }
    }

    public static Boolean isDefiniteNP(NounPhrase np) {
        if (np.getNpNode().getLeaves().get(0).toString().toLowerCase().equals("the")) {
            return true;
        } else {
            return false;
        }
    }

    public static Boolean isNotObject(NounPhrase np) {
        if (np.getNpNode().getLeaves().size() == 1 && NOT_OBJECTS.contains(np.getNpNode().getLeaves().get(0).toString().toLowerCase())) {
            return true;
        }
        return false;
    }

    //this, that, these, those
    public static Boolean isDemonstrativeNP(NounPhrase np) {
        return ((np.getNpNode().getLeaves().get(0).toString().toLowerCase().equals("this"))
                || (np.getNpNode().getLeaves().get(0).toString().toLowerCase().equals("that"))
                || (np.getNpNode().getLeaves().get(0).toString().toLowerCase().equals("these"))
                || (np.getNpNode().getLeaves().get(0).toString().toLowerCase().equals("those")));
    }

    public static int countDistance(NounPhrase np1, NounPhrase np2) {
        return Math.abs(np1.getSentenceId() - np2.getSentenceId());
    }

    public static Boolean isProperName(NounPhrase np) {
        //if the first letter of word is lettercase -> false, else continure checking
        if (Character.isUpperCase(np.getNpNode().getLeaves().get(0).toString().charAt(0))) {
            //if the NP is single word
            if (np.getNpNode().numChildren() == 1) {
                //if the NP is in the Dictionary -> false, else -> True
                if (sDict.contains(";" + np.getNpNode().getLeaves().get(0).toString().toLowerCase() + ";")) {
                    return false;
                } else {
                    return true;
                }
            } //if the NP is compound word
            else {
                // because "of" and "and" don't need to upper in Proper name, so check.
                for (int i = 0; i < np.getNpNode().getLeaves().size(); i++) {
                    if ((np.getNpNode().getLeaves().get(i).toString().equals("of"))
                            || (np.getNpNode().getLeaves().get(i).toString().equals("and"))
                            || (DETERMINERS.contains(np.getNpNode().getLeaves().get(i).toString().toLowerCase()))) {
                    } else {
                        if (Character.isLowerCase(np.getNpNode().getLeaves().get(i).toString().charAt(0))) {
                            return false;
                        } else {
                        }
                    }
                }
                return true;
            }
        } else {
            return false;
        }
    }

    public static Boolean isBothPropername(NounPhrase np1, NounPhrase np2) {
        if (hasProperName(np1, StanfordUtil.reviews.get(np1.getReviewId()).getSentences().get(np1.getSentenceId()))
                && hasProperName(np2, StanfordUtil.reviews.get(np2.getReviewId()).getSentences().get(np2.getSentenceId()))) {
            return true;
        } else {
            return false;
        }
    }

    //Check if the adjective is in the List of negative and positive words
    public static boolean checkAdjInList(String adj) {
        if (NEGATIVE_WORDS.contains(";" + adj + ";") || POSITIVE_WORDS.contains(";" + adj + ";")) {
            return true;
        } else {
            return false;
        }
    }

    public static Boolean hasBetweenExtract(Review review, NounPhrase np1, NounPhrase np2) {
        if (np1.getSentenceId() == np2.getSentenceId()) {
            Sentence curSentence = review.getSentences().get(np1.getSentenceId());
            if (np1.getOffsetEnd() < np2.getOffsetBegin()) {
                for (int i = 0; i < curSentence.getTokens().size(); i++) {
                    if (curSentence.getTokens().get(i).getOffsetBegin() > np1.getOffsetEnd()
                            && curSentence.getTokens().get(i).getOffsetEnd() < np2.getOffsetBegin()) {
                        if (curSentence.getTokens().get(i).getWord().equals("has")
                                || curSentence.getTokens().get(i).getWord().equals("have")
                                || curSentence.getTokens().get(i).getWord().equals("had")) {
                            if (curSentence.getTokens().get(i + 1).getWord().equals("to")
                                    || curSentence.getTokens().get(i + 1).getPOS().equals("VBN")) {
                                return false;
                            }
                            return true;
                        }
                    }
                }
            } else if (np2.getOffsetEnd() < np1.getOffsetBegin()) {
                for (int i = 0; i < curSentence.getTokens().size(); i++) {
                    if (curSentence.getTokens().get(i).getOffsetBegin() > np2.getOffsetEnd()
                            && curSentence.getTokens().get(i).getOffsetEnd() < np1.getOffsetBegin()) {
                        if (curSentence.getTokens().get(i).getWord().equals("has")
                                || curSentence.getTokens().get(i).getWord().equals("have")
                                || curSentence.getTokens().get(i).getWord().equals("had")) {
                            if (curSentence.getTokens().get(i + 1).getWord().equals("to")
                                    || curSentence.getTokens().get(i + 1).getPOS().equals("VBN")) {
                                return false;
                            }
                            return true;
                        }
                    }
                }
            }
        }

        return false;
    }

    public static Boolean hasBetween2Extract(Review review, NounPhrase np1, NounPhrase np2) {
        if (np1.getSentenceId() == np2.getSentenceId()) {
            if (hasNpBetween(np1, np2)) {
                return false;
            }
            Sentence curSentence = review.getSentences().get(np1.getSentenceId());
            if (np1.getOffsetEnd() < np2.getOffsetBegin()) {
                for (int i = 0; i < curSentence.getTokens().size(); i++) {
                    if (curSentence.getTokens().get(i).getOffsetBegin() > np1.getOffsetEnd()
                            && curSentence.getTokens().get(i).getOffsetEnd() < np2.getOffsetBegin()) {
                        if (curSentence.getTokens().get(i).getWord().equals("has")
                                || curSentence.getTokens().get(i).getWord().equals("have")
                                || curSentence.getTokens().get(i).getWord().equals("had")) {
                            if (curSentence.getTokens().get(i + 1).getWord().equals("to")
                                    || curSentence.getTokens().get(i + 1).getPOS().equals("VBN")) {
                                return false;
                            }
                            return true;
                        }
                    }
                }
            } else if (np2.getOffsetEnd() < np1.getOffsetBegin()) {
                for (int i = 0; i < curSentence.getTokens().size(); i++) {
                    if (curSentence.getTokens().get(i).getOffsetBegin() > np2.getOffsetEnd()
                            && curSentence.getTokens().get(i).getOffsetEnd() < np1.getOffsetBegin()) {
                        if (curSentence.getTokens().get(i).getWord().equals("has")
                                || curSentence.getTokens().get(i).getWord().equals("have")
                                || curSentence.getTokens().get(i).getWord().equals("had")) {
                            if (curSentence.getTokens().get(i + 1).getWord().equals("to")
                                    || curSentence.getTokens().get(i + 1).getPOS().equals("VBN")) {
                                return false;
                            }
                            return true;
                        }
                    }
                }
            }
        }

        return false;
    }

    //To compute the number of times a word appearing in the sentences
    public static int countWord(String word) {
        int matches = 0;

        for (int i = 0; i < WORDS.length; ++i) {
            if (WORDS[i].contains(" " + word.toLowerCase() + " ")) {
                ++matches;
            }
        }
        return matches;
    }

    //To compute the number of times 2 words appearing together in the sentences.
    public static int countTwoWords(NounPhrase np2, String ow) {
        int matches = 0;
        String np = np2.getHeadNode().toString();
        if (np2.getType() == 0){
        	for (int i = 0; i < WORDS.length; ++i) {
	            if ((WORDS[i].contains(" " + np.toLowerCase() + " ") || WORDS[i].contains(" phone "))
	                    && (WORDS[i].contains(" " + ow.toLowerCase() + " "))) {
	                ++matches;
	            }
	        }
        }
        else{
	        for (int i = 0; i < WORDS.length; ++i) {
	            if ((WORDS[i].contains(" " + np.toLowerCase() + " "))
	                    && (WORDS[i].contains(" " + ow.toLowerCase() + " "))) {
	                ++matches;
	            }
	        }
        }
        return matches;
    }

    public static int countSentences() {
        int counter = 0;
        for (int i = 0; i < Util.getDataset().length(); i++) {
            if (Util.getDataset().charAt(i) == '.') {
                counter++;
            }
        }
        return counter;
    }

    public static int probabilityNounPhrase(NounPhrase np) { 	
    	if (np.getType() == 0){
    		int matches = 0;
    		for (int i = 0; i < WORDS.length; ++i) {
            	if (WORDS[i].contains(" " + np.getHeadNode().toString().toLowerCase() + " ")
            			|| WORDS[i].contains(" phone "))
                    ++matches;
            } 
    		return matches;
    	}
    	else{
    		return countWord(np.getHeadNode().toString());
    	}
    }

    
    //PMI of NP2 and 1 OW of NP1
    public static Float onePMI(NounPhrase np2, String ow) {
        if ((probabilityNounPhrase(np2) == 0) || (countWord(ow) == 0) || (countTwoWords(np2, ow) == 0)) {
            return (float) 0;
        } else {
            return ((int) ((float) countTwoWords(np2, ow) / ((probabilityNounPhrase(np2) * countWord(ow))) * 100000000) / (float) 1000);
        }

    }
   //PMI of NP2 and all OWs of NP1
    public static Float PMI(NounPhrase np1, NounPhrase np2) {
        float sum = 0;
        for (String ow: np1.getOpinionWords())
        	sum = sum + onePMI(np2, ow);
        return sum;
    }

    
//    public static int countNPWithVerbBefore(NounPhrase np2, NounPhrase np1){
//    	 int matches = 0;
//    	 String word = "";
//    	 word += np1.getVerbBefore() + " ";
//    	 if (np2.getType() == 0)
//    		 word += "phone";
//    	 else
//    		 word += np2.getHeadNode().toString().toLowerCase();
//
//         for (int i = 0; i < WORDS.length; ++i) {
//             if (WORDS[i].contains(" " + word + " ")) {
//                 ++matches;
//             }
//         }
//         return matches;
//    }
//    
//    public static int countNPWithVerbAfter(NounPhrase np2, NounPhrase np1){
//   	 int matches = 0;
//   	 String word = "";
//   	 if (np2.getType() == 0)
//   		 word += "phone ";
//   	 else
//   		 word += np2.getHeadNode().toString().toLowerCase() + " ";
//   	 word += np1.getVerbBefore();
//        for (int i = 0; i < WORDS.length; ++i) {
//            if (WORDS[i].contains(" " + word + " ")) {
//                ++matches;
//            }
//        }
//        return matches;
//   }
    /**
     * ************************************
     * Some functions relates to String match
     *
     * **********************************
     */
    //Algorithm: N2 is considered that has a similar string with N1 if:
    //Main noun of N2 is the same as the main noun of N1 and
    //N1 includes all Nouns, adjactives of N2
    public static Boolean stringSimilarity(NounPhrase np1, NounPhrase np2, Sentence sen) {
        if (np2.getHeadNode().toString().toLowerCase().equals(np1.getHeadNode().toString().toLowerCase())) {
            for (Token token : sen.getTokens()) {
                if ((token.getOffsetBegin() >= np2.getOffsetBegin())
                        && token.getOffsetEnd() <= np2.getOffsetEnd()) {
                    if ((token.getPOS().equals("JJ"))
                            || (token.getPOS().equals("NN"))
                            || (token.getPOS().equals("NNS"))
                            || (token.getPOS().equals("NNP"))
                            || (token.getPOS().equals("NNPS"))) {
                        boolean isConclusion = false;
                        for (Tree tree : np1.getNpNode().getLeaves()) {
                            if (tree.toString().toLowerCase().equals(token.getWord().toLowerCase())) {
                                isConclusion = true;
                            }
                        }
                        if (isConclusion == false) {
                            return false;
                        }
                    }
                }
            }
            return true;

        }
        return false;
    }

    //Check if NP1 and NP2 are pronoun
    public static Boolean isBothPronoun(NounPhrase np1, NounPhrase np2) {
        if (isPronoun(np1) && isPronoun(np2)) {
            return true;
        }
        return false;
    }

    //Check if NP1 and NP2 are not pronoun and not proper name
    public static Boolean isBothNormal(NounPhrase np1, NounPhrase np2) {
        if ((!isPronoun(np1)) && (!isPronoun(np2)) && (!isProperName(np1)) && (!isProperName(np2))) {
            return true;
        }
        return false;
    }

    // Check if NP1 is substring of NP2 or inverse
    public static Boolean isSubString(NounPhrase np1, NounPhrase np2) {
        Sentence sen1 = StanfordUtil.reviews.get(np1.getReviewId()).getSentences().get(np1.getSentenceId());
        Sentence sen2 = StanfordUtil.reviews.get(np2.getReviewId()).getSentences().get(np2.getSentenceId());
        String str_np1 = " " + sen1.getRawContent().substring(np1.getOffsetBegin() - sen1.getOffsetBegin(), np1.getOffsetEnd() - sen1.getOffsetBegin()) + " ";
        String str_np2 = " " + sen2.getRawContent().substring(np2.getOffsetBegin() - sen2.getOffsetBegin(), np2.getOffsetEnd() - sen2.getOffsetBegin()) + " ";
        if (str_np1.toLowerCase().contains(str_np2.toLowerCase())
                || str_np2.toLowerCase().contains(str_np1.toLowerCase())) {
            return true;
        }
        return false;
    }

    // Check if the head noun of NP1 is the same with the head noun of NP2
    public static Boolean isHeadMatch(NounPhrase np1, NounPhrase np2) {
        if (np1.getHeadNode().toString().toLowerCase().equals(np2.getHeadNode().toString().toLowerCase())) {
            return true;
        }
        return false;
    }

    //Check if NP1 is the same with NP2
    public static Boolean isExactMatch(NounPhrase np1, NounPhrase np2) {
        if (np1.getCRFTokens().size() == np2.getCRFTokens().size()) {
            boolean checkIsExactMatch = true;
            for (int i = 0; i < np1.getCRFTokens().size(); i++) {
                if (!np1.getCRFTokens().get(i).getWord().toLowerCase().equals(np2.getCRFTokens().get(i).getWord().toLowerCase())) {
                    checkIsExactMatch = false;
                    break;
                }
            }
            if (checkIsExactMatch) {
                return true;
            }
        }
        return false;
    }

    //Check if NP1 is the same with NP2 after remove somes determines like: the, this, that, these, those, her,...
    public static Boolean isMatchAfterRemoveDeterminer(NounPhrase np1, NounPhrase np2) {
        Sentence sen1 = StanfordUtil.reviews.get(np1.getReviewId()).getSentences().get(np1.getSentenceId());
        Sentence sen2 = StanfordUtil.reviews.get(np2.getReviewId()).getSentences().get(np2.getSentenceId());
        String str_np1 = sen1.getRawContent().substring(np1.getOffsetBegin() - sen1.getOffsetBegin(), np1.getOffsetEnd() - sen1.getOffsetBegin());
        String str_np2 = sen2.getRawContent().substring(np2.getOffsetBegin() - sen2.getOffsetBegin(), np2.getOffsetEnd() - sen2.getOffsetBegin());
        if (str_np1.contains(" ")) {
            if (DETERMINERS.contains(str_np1.substring(0, str_np1.indexOf(" ")).toLowerCase())) {
                str_np1 = str_np1.substring(str_np1.indexOf(" ") + 1);
            }
        }
        if (str_np2.contains(" ")) {
            if (DETERMINERS.contains(str_np2.substring(0, str_np2.indexOf(" ")).toLowerCase())) {
                str_np2 = str_np2.substring(str_np2.indexOf(" ") + 1);
            }
        }
        if (str_np1.toLowerCase().equals(str_np2.toLowerCase())) {
            return true;
        }
        return false;
    }

    public static boolean isPhoneHead(NounPhrase np1, NounPhrase np2) {
        return (np1.getHeadNode().toString().toLowerCase().equals("phone")
                || np2.getHeadNode().toString().toLowerCase().equals("phone"));
    }

    /**
     * *******************
     * End some functions relating to String match ********************
     */
    public static int sentimentConsistencyExtract(NounPhrase np1, NounPhrase np2) {
        if (np1.getReviewId() == np2.getReviewId()) {
            //Only consider the case NP1 and NP2 are contained in 2 consecutive sentences
            if (Math.abs(np1.getSentenceId() - np2.getSentenceId()) == 1) {
                NounPhrase npStandBefore = np1.getSentenceId() - np2.getSentenceId() == 1 ? np2 : np1;
                NounPhrase npStandAfter = np1.getSentenceId() - np2.getSentenceId() == 1 ? np1 : np2;
                //The sentence of NP1 is a comparative sentence. The sentence of NP2 is a normal sentence
                Sentence npBeforeSentence = StanfordUtil.reviews.get(npStandBefore.getReviewId()).getSentences().get(npStandBefore.getSentenceId());
                if (npBeforeSentence.isComparativeSentence()) {
                    Sentence npAfterSentence = StanfordUtil.reviews.get(npStandAfter.getReviewId()).getSentences().get(npStandAfter.getSentenceId());
                    if (!npAfterSentence.isComparativeSentence()) {
                        //Only consider the case NP2 is a candidate
//                        if (npStandAfter.getType() == 2) {
                            //NP1 and NP2 have the same orientation
                            if ((npStandBefore.isSuperiorEntity() && npStandAfter.getSentimentOrientation() == Util.POSITIVE)
                                    || (npStandBefore.isInferiorEntity() && npStandAfter.getSentimentOrientation() == Util.NEGATIVE)) {
                                return 1;
                            } 
                            else if ((npAfterSentence.getRawContent().trim().startsWith("However")                                            
                                            || npAfterSentence.getRawContent().trim().startsWith("But"))
                                    && ((npStandBefore.isInferiorEntity() && npStandAfter.getType() == 2 && npStandAfter.getSentimentOrientation() == Util.POSITIVE)
                                    || (npStandBefore.isSuperiorEntity() && npStandAfter.getSentimentOrientation() == Util.NEGATIVE))) {
                                return 1;
                            }
                            //NP1 and NP2 don't have the same orientation
                            else if ((npStandBefore.isInferiorEntity() && npStandAfter.getType() == 2 && npStandAfter.getSentimentOrientation() == Util.POSITIVE)
                                    || (npStandBefore.isSuperiorEntity() && npStandAfter.getSentimentOrientation() == Util.NEGATIVE)) {
                                return 0;
                            }
//                        }
                    }
                } //Both the sentences of NP1 and NP2 are normal sentences                
                else if (!npBeforeSentence.isComparativeSentence()) {
                    Sentence npAfterSentence = StanfordUtil.reviews.get(npStandAfter.getReviewId()).getSentences().get(npStandAfter.getSentenceId());
                    if (!npAfterSentence.isComparativeSentence()) {
                        //Sentiment toward NP1 and NP2 must be the same. If not, return 2
                        if (npStandAfter.getSentimentOrientation() != Util.NEUTRAL
                                && npStandBefore.getSentimentOrientation() != Util.NEUTRAL) {
                            //NP standing before is an object
                            if (npStandBefore.getType() == 0) {
                                //NP standing after is a candidate
//                                if (npStandAfter.getType() == 2) {
                                    if (npStandAfter.getSentimentOrientation() == npStandBefore.getSentimentOrientation()) {
                                        return 1;
                                    } else if (npStandAfter.getSentimentOrientation() != npStandBefore.getSentimentOrientation()
                                            && (npAfterSentence.getRawContent().startsWith("However") 
                                            || npAfterSentence.getRawContent().startsWith(" However")
                                            || npAfterSentence.getRawContent().startsWith("But")
                                            || npAfterSentence.getRawContent().startsWith(" But"))) {
                                        return 1;
                                    } 
                                    else if (npStandAfter.getSentimentOrientation() != npStandBefore.getSentimentOrientation()) {
                                        return 0;
                                    }
//                                }
                            }
                        }
                    }

                }
            }
        }
        return 2;
    }

    public static Boolean isNested(NounPhrase np1, NounPhrase np2) {
        if ((np1.getOffsetBegin() >= np2.getOffsetBegin() && np1.getOffsetEnd() <= np2.getOffsetEnd())
                || (np2.getOffsetBegin() >= np1.getOffsetBegin() && np2.getOffsetEnd() <= np1.getOffsetEnd())) {
            return true;
        } else {
            return false;
        }
    }

    public static Boolean isNNP(Token tk) {
        if (Character.isUpperCase(tk.getWord().charAt(0))) {
            return true;
        }
        if (tk.getWord().toLowerCase().equals("iphone")
                || tk.getWord().toLowerCase().equals("ipad")
                || tk.getWord().toLowerCase().equals("iphones")
                || tk.getWord().toLowerCase().equals("ipads")
                || tk.getWord().toLowerCase().equals("ipod")) {
            return true;
        }
        return false;
    }

    public static Boolean hasProperName(NounPhrase np, Sentence se){
    	if (!Character.isUpperCase(np.getHeadNode().toString().charAt(0))
    			&& !(Character.isDigit(np.getHeadNode().toString().charAt(0)))){
    		if (np.getHeadNode().toString().toLowerCase().equals("iphone")
                    || np.getHeadNode().toString().toLowerCase().equals("ipad")
                    || np.getHeadNode().toString().toLowerCase().equals("iphones")
                    || np.getHeadNode().toString().toLowerCase().equals("ipads")
                    || np.getHeadNode().toString().toLowerCase().equals("ipod")) {
                return true;
            }
    			return false;
    	}
    	else if (np.getHeadLabel().equals("NNP") || np.getHeadLabel().equals("NNPS") || np.getHeadLabel().equals("CD"))
    		return true;
    	else {
    		if (!sDict.contains(np.getHeadNode().toString().toLowerCase()))
    			return true;
    		else{	
    			if (np.getCRFTokens().size() > 1)
    				return true;
    			else 
    				return false;
    		}
    	}
    			
    }
    
//    public static Boolean hasProperName(NounPhrase np, Sentence se) {
//        ArrayList<String> aList = new ArrayList<String>();
//        ArrayList<Integer> aListOfCheck = new ArrayList<Integer>();
//        for (int i = 0; i < se.getTokens().size(); i++) {
//            if ((se.getTokens().get(i).getOffsetBegin() >= np.getOffsetBegin()) && (se.getTokens().get(i).getOffsetBegin() < np.getOffsetEnd())) {
//                if (isNNP(se.getTokens().get(i))) {
//                    aList.add(se.getTokens().get(i).getWord());
//                    if (se.getTokens().get(i).getPOS().toString().equals("NNP")
//                            || se.getTokens().get(i).getPOS().toString().equals("NNPS")) {
//                        aListOfCheck.add(1);
//                    } else {
//                        aListOfCheck.add(0);
//                    }
//                } else if (i > 0 && i < (se.getTokens().size() - 1)) {
//                    if (Character.isDigit(se.getTokens().get(i).getWord().charAt(0))) {
//                        if (se.getTokens().get(i - 1).getPOS().equals("DT")
//                                || se.getTokens().get(i - 1).getPOS().equals("NNP")
//                                || se.getTokens().get(i - 1).getPOS().equals("NNPS")
//                                || Character.isUpperCase(se.getTokens().get(i - 1).getWord().charAt(0))) {
//                            if ((se.getTokens().get(i + 1).getPOS().equals("NN"))
//                                    || (se.getTokens().get(i + 1).getPOS().equals("NNS"))
//                                    || (se.getTokens().get(i + 1).getPOS().equals("JJ"))) {
//                            } else {
//                                aList.add(se.getTokens().get(i).getWord());
//                                aListOfCheck.add(1);
//                            }
//
//                        }
//                    }
//
//                }
//            }
//        }
//        if (aList.size() == 0) {
//            return false;
//        } else {
//            for (int i = 0; i < aList.size(); i++) {
//                if (aListOfCheck.get(i).equals(1)) {
//                    for (String st1 : aList) {
//                    }
//                    return true;
//                } else {
//                    boolean checkContains = true;
//                    if (sDict.contains(aList.get(i).toLowerCase())) {
//                        checkContains = false;
//                    } else if (check_adj_in_list(aList.get(i))) {
//                        checkContains = false;
//                    }
//
//                    if (checkContains) {
//                        return true;
//                    }
//                }
//            }
//
//            return false;
//
//        }
//    }
    
    //Word starts a relative clause: that, which, //
    public static boolean isRelativePronounNPs(NounPhrase np1, NounPhrase np2){
        if (np1.getReviewId() == np2.getReviewId()){
            if (np1.getSentenceId() == np2.getSentenceId()){
                int np1OffsetBegin = np1.getOffsetBegin();
                int np2OffsetBegin = np2.getOffsetBegin();                
                NounPhrase npBefore = np1OffsetBegin > np2OffsetBegin? np2: np1;
                NounPhrase npAfter = np1OffsetBegin > np2OffsetBegin? np1: np2;
                
                //Only consider the case that the NP after stands after the NP before
                if (npAfter.getId() - npBefore.getId() == 1){
                    List<CRFToken> npCrfTokens = npAfter.getCRFTokens();
                    if (npCrfTokens.size() == 1){
                        List<Token> npSentTokens = StanfordUtil.reviews.get(npAfter.getReviewId()).getSentences().get(npAfter.getSentenceId()).getTokens();
                        if (npSentTokens.get(npCrfTokens.get(0).getIdInSentence()).isRelativePronoun()){
                            return true;
                        }                
                    }
                }
            }
        }
        return false;
    }

    //Check if the adjective is in the List of negative and positive words
    public static boolean check_adj_in_list(String adj) {
        if (NEGATIVE_WORDS.contains(";" + adj.toLowerCase() + ";") || POSITIVE_WORDS.contains(";" + adj.toLowerCase() + ";")
                || EXTRA_WORDS.contains(";" + adj.toLowerCase() + ";")) {
            return true;
        } else {
            return false;
        }
    }

    //Check case "as well"
    public static boolean checkSpecialCase(Sentence fSentence, Token tk) {
        if (tk.getWord().equals("well")) {
            for (int i = fSentence.getTokens().size() - 1; i >= 0; i--) {
                if (fSentence.getTokens().get(i).getOffsetEnd() < tk.getOffsetBegin()) {
                    if (fSentence.getTokens().get(i).getWord().equals("as")) {
                        return true;
                    }
                    break;
                }
            }
        }
        return false;
    }
    

    public static void setNPForOPInSentence(Sentence fSentence) {
        int no = 0;
        for (Token token : fSentence.getTokens()) {
            no++;
            if (Character.isUpperCase(token.getWord().charAt(0))) {
                if (no == 1) {
                } else {
                    continue;
                }
            }
            // get opinion word in the sentence
            if (token.getPOS().equals("RB")
                    && (check_adj_in_list(token.getWord()))
                    && !checkSpecialCase(fSentence, token)) {
                for (int i = fSentence.getNounPhrases().size() - 1; i >= 0; i--) {
                    boolean checkFind = false;
                    if (fSentence.getNounPhrases().get(i).getOffsetEnd() < token
                            .getOffsetBegin()) {
                        for (Token tk : fSentence.getTokens()) {
                            if ((tk.getOffsetBegin() >= fSentence
                                    .getNounPhrases().get(i).getOffsetEnd())
                                    && (tk.getOffsetEnd() <= token
                                    .getOffsetBegin())) {
                                if (tk.getPOS().equals("VB")
                                        || tk.getPOS().equals("VBD")
                                        || tk.getPOS().equals("VBG")
                                        || tk.getPOS().equals("VBN")
                                        || tk.getPOS().equals("VBP")
                                        || tk.getPOS().equals("VBZ")) {
                                    if (!Util.isDiscardedQuantityNP(fSentence
                                            .getNounPhrases().get(i))) {
                                        checkFind = true;
                                        break;
                                    }
                                }
                            }
                        }
                        if (checkFind == true) {
                            fSentence.getNounPhrases().get(i)
                                    .addOpinionWord(token.getWord());
                        }
                    }
                    if (checkFind == true) {
                        break;
                    }
                }
            } else if ((token.getPOS().equals("JJ")
                    || token.getPOS().equals("JJS") || token
                    .getPOS().toString().equals("JJR"))
                    && (check_adj_in_list(token.getWord()))) {
                boolean check = false;
                // if OW is nested in NP -> OW belongs to that NP
                for (NounPhrase np : fSentence.getNounPhrases()) {
                    if (np.getOffsetBegin() <= token.getOffsetBegin()
                            && np.getOffsetEnd() >= token.getOffsetEnd()) {
                        if (!Util.isDiscardedQuantityNP(np)) {
                            np.addOpinionWord(token.getWord());
                            check = true;
                        }
                    }
                }

                if (check == false) {
                    // If the sentence is a exclamatory sentence
                    if ((fSentence.getRawContent().indexOf(
                            "how " + token.getWord()) != -1)
                            || (fSentence.getRawContent().indexOf(
                                    "How " + token.getWord()) != -1)) {
                        for (NounPhrase np : fSentence.getNounPhrases()) {
                            if (np.getOffsetBegin() > token.getOffsetEnd()) {
                                if (!Util.isDiscardedQuantityNP(np)) {
                                    np.addOpinionWord(token.getWord()
                                            .toString());
                                    break;
                                }
                            }
                        }
                    } else {
                        boolean check1 = false;
                        for (NounPhrase np : fSentence.getNounPhrases()) {
                            if ((np.getOffsetBegin() - token.getOffsetEnd()) > 0) {
                                if ((np.getOffsetBegin() - token.getOffsetEnd()) <= 1) {
                                    if (!Util.isDiscardedQuantityNP(np)) {
                                        check1 = true;
                                        np.addOpinionWord(token.getWord()
                                                .toString());
                                        break;
                                    }
                                }
                            }
                        }
                        if (check1 == false) {
                            for (int j = fSentence.getNounPhrases().size() - 1; j >= 0; j--) {
                                boolean checkFindAdj = false;
                                if (fSentence.getNounPhrases().get(j)
                                        .getOffsetEnd() < token
                                                .getOffsetBegin()) {
                                    for (int k = 0; k < fSentence.getTokens().size(); k++) {
                                    	Token tk = fSentence.getTokens().get(k);
                                        if ((tk.getOffsetBegin() >= fSentence
                                                .getNounPhrases().get(j)
                                                .getOffsetEnd())
                                                && (tk.getOffsetEnd() <= token
                                                .getOffsetBegin())) {
                                            if (tk.getPOS()
                                                    .equals("VB")
                                                    || tk.getPOS()
                                                            .equals("VBD")
                                                    || tk.getPOS()
                                                            .equals("VBG")
                                                    || tk.getPOS()
                                                            .equals("VBN")
                                                    || tk.getPOS()
                                                            .equals("VBP")
                                                    || tk.getPOS()
                                                            .equals("VBZ")) {
                                                if (!Util
                                                        .isDiscardedQuantityNP(fSentence
                                                                .getNounPhrases()
                                                                .get(j))) {
                                                    checkFindAdj = true;
                                                    break;
                                                }
                                            }
                                        }
                                        
                                        if (tk.getOffsetEnd() < fSentence.getNounPhrases().get(j).getOffsetBegin()
                                        		&& (tk.getWord().equals("make") || tk.getWord().equals("makes"))){
                                        	checkFindAdj = true;
                                            break;
                                        }
                                    }
                                    if (checkFindAdj) {
                                        fSentence
                                                .getNounPhrases()
                                                .get(j)
                                                .addOpinionWord(token.getWord());
                                        break;
                                    }
                                }
                                if (checkFindAdj) {
                                    break;
                                }
                            }
                        }
                    }
                }
            }
        }
    }
    
    
    public static int sentimentConsistencyExtract2(NounPhrase np1, NounPhrase np2) {
        if (np1.getReviewId() == np2.getReviewId()) {
            //Only consider the case NP1 and NP2 are contained in 2 consecutive sentences
            if (Math.abs(np1.getSentenceId() - np2.getSentenceId()) == 1) {
            	if ((np1.getType() == 0 || np1.getType() == 3) && (np2.getType() == 0 || np2.getType() == 3))
            		return 4;
            	else{
                NounPhrase npStandBefore = np1.getSentenceId() - np2.getSentenceId() == 1 ? np2 : np1;
                NounPhrase npStandAfter = np1.getSentenceId() - np2.getSentenceId() == 1 ? np1 : np2;
                //The sentence of NP1 is a comparative sentence. The sentence of NP2 is a normal sentence
                Sentence npBeforeSentence = StanfordUtil.reviews.get(npStandBefore.getReviewId()).getSentences().get(npStandBefore.getSentenceId());
                if (npBeforeSentence.isComparativeSentence()) {
                    Sentence npAfterSentence = StanfordUtil.reviews.get(npStandAfter.getReviewId()).getSentences().get(npStandAfter.getSentenceId());
                    if (!npAfterSentence.isComparativeSentence()) {
                        //Only consider the case NP2 is a candidate
//                        if (npStandAfter.getType() == 2) {
                            //NP1 and NP2 have the same orientation
                            if ((npStandBefore.isSuperiorEntity() && npStandAfter.getSentimentOrientation() == Util.POSITIVE)
                                    || (npStandBefore.isInferiorEntity() && npStandAfter.getSentimentOrientation() == Util.NEGATIVE)) {
                                return 1;
                            } 
                            else if ((npAfterSentence.getRawContent().trim().startsWith("However")                                            
                                            || npAfterSentence.getRawContent().trim().startsWith("But"))
                                    && ((npStandBefore.isInferiorEntity() && npStandAfter.getType() == 2 && npStandAfter.getSentimentOrientation() == Util.POSITIVE)
                                    || (npStandBefore.isSuperiorEntity() && npStandAfter.getSentimentOrientation() == Util.NEGATIVE))) {
                                return 1;
                            }
                            //NP1 and NP2 don't have the same orientation
                            else if ((npStandBefore.isInferiorEntity() && npStandAfter.getType() == 2 && npStandAfter.getSentimentOrientation() == Util.POSITIVE)
                                    || (npStandBefore.isSuperiorEntity() && npStandAfter.getSentimentOrientation() == Util.NEGATIVE)) {
                                return 0;
                            }
//                        }
                    }
                } //Both the sentences of NP1 and NP2 are normal sentences                
                else if (!npBeforeSentence.isComparativeSentence()) {
                    Sentence npAfterSentence = StanfordUtil.reviews.get(npStandAfter.getReviewId()).getSentences().get(npStandAfter.getSentenceId());
                    if (!npAfterSentence.isComparativeSentence()) {
                        //Sentiment toward NP1 and NP2 must be the same. If not, return 2
                        if (npStandAfter.getSentimentOrientation() != Util.NEUTRAL
                                && npStandBefore.getSentimentOrientation() != Util.NEUTRAL) {
                            //NP standing before is an object
                            if (npStandBefore.getType() == 0) {
                                //NP standing after is a candidate
//                                if (npStandAfter.getType() == 2) {
                                    if (npStandAfter.getSentimentOrientation() == npStandBefore.getSentimentOrientation()) {
                                        return 1;
                                    } else if (npStandAfter.getSentimentOrientation() != npStandBefore.getSentimentOrientation()
                                            && (npAfterSentence.getRawContent().startsWith("However") 
                                            || npAfterSentence.getRawContent().startsWith(" However")
                                            || npAfterSentence.getRawContent().startsWith("But")
                                            || npAfterSentence.getRawContent().startsWith(" But"))) {
                                        return 1;
                                    } 
                                    else if (npStandAfter.getSentimentOrientation() != npStandBefore.getSentimentOrientation()) {
                                        return 0;
                                    }
//                                }
                            }
                        }
                    }
                }
                }
            }
            else
            	return 3;
        }
        return 2;
    }
}
