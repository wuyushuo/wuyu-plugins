/*--------------------------------------------------------------------------
 *  Copyright (c) 2009-2020, www.wuyushuo.com All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met:
 *
 * Redistributions of source code must retain the above copyright notice,
 * this list of conditions and the following disclaimer.
 * Redistributions in binary form must reproduce the above copyright
 * notice, this list of conditions and the following disclaimer in the
 * documentation and/or other materials provided with the distribution.
 * Neither the name of the yinyuetai developer nor the names of its
 * contributors may be used to endorse or promote products derived from
 * this software without specific prior written permission.
 * Author: git_wuyu@163.com (tencent qq: 2094998377)
 *--------------------------------------------------------------------------
*/
package com.wuyu.plugin.wfilter.filter;

import com.wuyu.plugin.wfilter.algorithm.TreeNode;
import com.wuyu.plugin.wfilter.load.WConfs;
import com.wuyu.plugin.wfilter.load.WFLoad;

import java.util.*;

/**
 * class function depict
 * organization <a href="http://www.wuyushuo.com">www.wuyushuo.com</a>
 * created on 2016/02/06 by <strong>elon su</strong>
 * email addr (<a href='mailto:git_wuyu@163.com'>git_wuyu@163.com</a>)
 * @version 1.0.0
 */
public class WordFilter implements WFilter {

    private String replaceTo = WConfs.CONF_KEY_REPLACE_TO_VALUE;

    private TreeNode treeNode;

    private List<WordToken> wordTokens = new ArrayList<WordToken>();


    /**
     * load from component
     */
    public WordFilter(){
        WFLoad wfLoad = WFLoad.getWFLoad();
        this.replaceTo = wfLoad.getFilterReplaceTo();
        treeNode = TreeNode.initNode(wfLoad.getFilterWords());
    }

    /**
     * for self daynamic ext
     * @param words
     */
    public WordFilter(Set<String> words) {
        treeNode = TreeNode.initNode(words);
    }

    /**
     * for self daynamic ext
     * @param words
     */
    public WordFilter(String[] words){
        Set<String> sets = new HashSet<String>();
        for(int i=0; i<words.length; i++){
            sets.add(words[i]);
        }
        treeNode = TreeNode.initNode(sets);
    }

    /**
     * search word from tree node
     * @param word
     * @param root
     */
    public void search(String word, TreeNode root) {
        char[] chats = word.toCharArray();
        TreeNode node = root;
        int count = 0;
        for (int i = 0; i < chats.length;) {
            char chat = chats[i + count];
            count++;
            node = findNode(node, chat);
            if (node == null) {
                node = root;
                i++;
                count = 0;
                continue;
            } else {
                if (node.isEnd) {
                    {
                        WordToken wordToken = new WordToken();
                        wordToken.pos = i;
                        wordToken.word = node.word;
                        wordTokens.add(wordToken);
                    }

                    i += node.word.length();
                    node = root;
                    count = 0;

                }
            }
        }
    }



    /**
     * node find
     * @param node
     * @param chat
     * @return
     */
    private TreeNode findNode(TreeNode node, char chat) {
        if (node.tree.containsKey(chat)) {
            Map<Character, TreeNode> map = node.tree.get(chat);
            return map.get(chat);
        }
        return null;
    }


    /**
     * do content replace
     * @param content
     * @return
     */
    public String process(String content){
        return process(content, replaceTo);
    }


    /**
     * do content replace
     * @param content
     * @param replaceTo
     * @return
     */
    @Override
    public String process(String content, String replaceTo) {
        search(content, treeNode);
        StringBuilder builder = new StringBuilder(content);
        for (int i = wordTokens.size() - 1; i >= 0; i--) {
            WordToken token = wordTokens.get(i);
            builder.replace(token.pos, token.end(), replaceTo);
        }
        return builder.toString();
    }



    /**
     * term
     */
    class WordToken {

        public String word;
        public int pos = -1;

        public int end() {
            return pos + word.length();
        }

    }



    public static void main(String[] args){

        String content = "最近搞到一本黄色小说，《维奥莱特罗曼史》，作者马努里伯爵夫人，一个法国女人，\n"
                + "该书1870年问世，那是大仲马、小仲马、福楼拜、莫泊桑等人叱咤文坛的时代。这个小说不太长，\n" + "只有一百零几页，一个中篇的篇幅。短是黄色小说的通病，比如《如意君传》只是一个小短篇，篇幅跟唐代的传奇差不多，\n"
                + "《宜春香质》短短二十回，却是四个男子同性恋的故事的汇编。我的电脑里有十几部古典黄色小说，都是以HTML格式保存的，\n"
                + "占硬盘面积最大的是《肉蒲团》，将近二百K，不到十万字《维奥莱特罗曼史》讲的是缝纫店的小女工维奥莱特不堪男老板的骚扰，\n"
                + "深夜投奔楼上的画家房客克里斯蒂昂先生，此人是这个故事的叙述者，他把维奥莱特以金屋藏之，对她进行性启蒙，\n" + "并且享受了她迫不及待交出的初夜。没过两天，维奥莱特曾经的顾客奥代特伯爵夫人辗转找到维奥莱特，\n"
                + "伯爵夫人有同性恋倾向，曾经借试衣之便，跟当时还不谙风情的维奥莱特有过亲昵。维奥莱特在克里斯蒂昂的怂恿和策划下，\n" + "与饥渴的伯爵夫人发展出了真正的同性恋关系，伯爵夫人扮演主动的积极的进攻性的男性角色。\n"
                + "其后克里斯蒂昂参与了进来，达成了三人共同寻欢作乐的友好协议。这是小说的上半部分。\n" + "下半部分叙述的是，维奥莱特想当戏剧演员，克里斯蒂昂先是给她找了几个男老师，可是他们都有做情夫的愿望，\n"
                + "后来找到了同性恋演员费洛朗丝才告一段落。（就像管理学界的余世伟讲的，要找做清洁的保姆，\n" + "没有必要找一个不讲个人卫生的，然后再费心费力地对她进行卫生培训，直接找一个有洁癖的人就行了。）\n"
                + "接下来是伯爵夫人色诱长了胸毛的费洛朗丝，不过这次她扮演的是女性角色。结局是维奥莱特感染了风寒，\n" + "过早地凋零了，叙述者多年之后死掉了上了火星仍然感伤不已。这本黄色小说写的相当出色。说到这儿，\n"
                + "怕是要顺手打击一些其它的黄色小说了。叙事者这么评价维奥莱特的：确切地来说，她是激情型的，\n" + "而不是放荡型的，在我认识她的三年内，无论两人也好，三人也好，我们在做爱方面使出了浑身解数，\n"
                + "却从未听见她嘴里冒出一句粗话。女人可以分为激情型与放荡型，黄色小说也可以划分为激情型和放荡型两种。\n" + "你要是错过了前者，人生是不完整的；你要是没有碰到后者呢，那也没有什么好遗憾的。\n"
                + "激情型和放荡型是一种简单的划分，下面我尝试着大体标出一些界限来：做爱的时候说不说粗话，\n" + "做爱的时候喊不喊叫生殖器只是区别之一。即二者有粗俗与优雅之别。其二，如果作者使用大量的形容词、\n"
                + "成语还有排比句，来修饰肉体、性器官和性感受，那么这本书可以扔掉了，它肯定是不入流的放荡型黄色小说，\n" + "因为性交和叙述性交是两码事，很少有把性交时的迷狂合适地带到文本中去的。即二者有铺张与节制之别。\n"
                + "其三，《维奥莱特罗曼史》中的叙述者有句话，说是成为维奥莱特的情夫，就像从字母表到达Z，\n" + "吻手只是A。这也是放荡型黄色小说与激情型黄色小说的区别。放荡型很多时候一步到位，直接杀到了Z，\n"
                + "激情型的按部就班照章办事，唱、念、做、打一丝不苟丝毫不逾矩。换句话说，同样是为了激起读者的性欲，\n" + "引起读者的生殖共鸣，放荡型黄色小说就像是一个急躁的色鬼，或者说是志在必得的强奸犯，用的是动作片的手法，\n"
                + "重点放在性交的动作展示上面，而激情型的黄色小说则是一个老道的调情高手，或者说是一个精心布局用心良苦的谋杀犯，\n" + "用的是剧情片的手法，重点突出欲望从产生到实现的全过程。\n"
                + "即二者有结果主义与过程主义之别（或快慢之别，或简繁之别）。上面三条形式判断，在时间成为稀缺资源的今天，\n" + "以此为据，我们可以避开许多一文不值的放荡型黄色小说，然后腾出精力来尽情享受激情型黄色小说，\n"
                + "比如这本《维奥莱特罗曼史》。除了优雅、节制、过程主义的形式优点之外，它拥有更多的实质优点，\n" + "比如为了获得性交机会，主人公投入了巨大的精神力量，克里斯蒂昂巧妙地切断了维奥莱特与旧日生涯的联系，\n"
                + "又环环相扣欲擒故纵地让伯爵夫人加入了三人性交小组，还有伯爵夫人步步为营获得了费洛朗丝的同性爱。\n"
                + "激情型黄色小说的主人公不光是体力劳动者，他们更是脑力劳动者，而且他们的脑力劳动往往是为人类现有的知识积累再谱华章，" + "而不是丢现有知识成果的脸。\n"
                + "其次，我们从作者的层面来谈。就像是柏拉图在大学前立了个不懂几何者拒绝入内的标牌，\n"
                + "《维奥莱特罗曼史》这样激情型黄色小说的存在，无异于为黄色小说的创作者树了门槛：并不是你多色、多浪、多粗、多大、多长、多深、多潮、多有暴露欲和倾诉欲，"
                + "就能写好黄色小说的，它还与你对智性的拥有量和支配能力正相关。\n" + "关于女性、关于人性、关于性，作者是马努里伯爵夫人不时有相当机智的论述，比如演员费洛朗丝说起自己继承了父亲的学问时说，"
                + "我的学问于我何用呢，大部分时间我用来轻视我演出的作品。她宣扬自己的女权观念时概括地说，就是不给其他任何人以权利对您说，" + "我要。前一句对演艺界的无知风尚不无讽喻意味，后一句对女权主义观念的阐发简明而精当。\n"
                + "所以，激情型黄色小说的作者不仅能激起读者的性欲，还能把读者的一部分性欲导引到大脑，先分解掉再暗中存储起来。\n"
                + "而放荡型黄色小说的作者是只管放火，放得起就放，放不起拉倒，放起了就不管了，他们不会像弗洛伊德那样想到力比多的升华。\n"
                + "概括一下放荡型黄色小说文本内外观念就是，认为做爱不用脑子，描写做爱时也不用脑子，还指望着看黄色小说的人也不用脑子。\n"
                + "之于激情型黄色小说，用《维奥莱特罗曼史》中评价法国雕塑家普拉迪埃的作品的话来说就是：这富有魔力的雕塑家，能使贞淑女子的雕像也变得撩人。";

        WordFilter wFilter = new WordFilter();

        // 这里重写所有配置规则
        System.out.println(wFilter.process(content, "*"));

        // 使用默认的配置规则
        System.out.println(wFilter.process(content));
    }
}
