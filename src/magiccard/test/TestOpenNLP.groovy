/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package magiccard.test

import opennlp.tools.postag.POSModel
import opennlp.tools.postag.POSTaggerME
import opennlp.tools.tokenize.TokenizerModel
import opennlp.tools.tokenize.Tokenizer
import opennlp.tools.tokenize.TokenizerME

def text="""
Pierre Vinken , 61 years old , will join the board as a nonexecutive director Nov. 29 .
Mr. Vinken is chairman of Elsevier N.V. , the Dutch publishing group .
"""

def nlpDataDir = "D:\\DEV\\Java\\TextProcessing\\OpenNLP\\Data\\"

InputStream tokenModelIn = new FileInputStream(nlpDataDir+"en-token.bin");
TokenizerModel tokenModel = null;

try {
  tokenModel = new TokenizerModel(tokenModelIn);
}
catch (IOException e) {
  e.printStackTrace();
}
finally {
  if (tokenModelIn != null) {
    try {
      tokenModelIn.close();
    }
    catch (IOException e) {
    }
  }
}
Tokenizer tokenizer = new TokenizerME(tokenModel);

def tokens = tokenizer.tokenize(text);

InputStream posModelIn = null;
POSModel posModel = null;
try {
  posModelIn = new FileInputStream(nlpDataDir+"en-pos-maxent.bin");
  posModel = new POSModel(posModelIn);
}
catch (IOException e) {
  // Model loading failed, handle the error
  e.printStackTrace();
}
finally {
  if (posModelIn != null) {
    try {
      posModelIn.close();
    }
    catch (IOException e) {
    }
  }
}

def tagger = new POSTaggerME(posModel);

def sent = ["Most", "large", "cities", "in", "the", "US", "had",
                             "morning", "and", "afternoon", "newspapers", "."];		  
def tags = tagger.tag(tokens);
def probs = tagger.probs();

tags.eachWithIndex{tag,index->
    println tokens[index] + ' _' + tag + ' : '+probs[index];
}

