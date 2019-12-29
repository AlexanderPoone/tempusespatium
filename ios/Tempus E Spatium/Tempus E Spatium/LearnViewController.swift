//
//  LearnViewController.swift
//  Tempus E Spatium
//
//  Created by Victor Poon on 15/10/2019.
//  Copyright © 2019 SoftFeta. All rights reserved.
//

import UIKit
import WebKit
import SwiftIcons

class LearnViewController: UIViewController {

    private var mTimer:Timer?
    private var mSecs = 3
    
    @IBOutlet weak var mHeader: UILabel!
    
    @IBOutlet weak var mWebView: WKWebView!
    
    @IBOutlet weak var mThemeLbl: UILabel!
    var mUrl:String?
    
    @IBOutlet weak var mSolarized: UIView!
    @IBOutlet weak var mWhite: UIView!
    @IBOutlet weak var mDark: UIView!
    
    private let mPreferences = UserDefaults.standard
    
    @objc func hideStuff() {
        mSecs -= 1
        if mSecs < 0 {
            mTimer!.invalidate()
            mTimer = nil
            let JS2 = """
            javascript:(function() { $(document).ready(function(){ $('.ambox').hide();$('.reference').hide();$('.hatnote').hide();$('#mw-mf-page-center > header > form').hide();$('span.mw-editsection > a').hide();$('#content > div.pre-content.heading-holder > nav').hide();$('#mw-mf-page-center > footer').hide();$('a:not(.image):not(.mw-ui-icon)').replaceWith(function(){return $('<strong style=\"color:olive;\">').append($(this).html());}); }); })()
            """
            mWebView.evaluateJavaScript(JS2, completionHandler: nil)
            mWebView.isHidden = false
        }
    }
    
    @objc func mSolarizedClicked() {
//        hideStuff()
                mHeader.setIcon(prefixText: "", prefixTextColor: .white, icon: .fontAwesomeSolid(.book), iconColor: UIColor(named: "warning")!, postfixText: NSLocalizedString("learn", comment: ""), postfixTextColor: UIColor(named: "warning")!, size: nil, iconSize: nil)
        view.backgroundColor = UIColor(named: "CosmicLatte")!
        mThemeLbl.textColor = UIColor(named: "warning")!
        mPreferences.set(0, forKey: "PREF_LEARNING_THEME")
        mPreferences.synchronize()
        
        let JS = "javascript:(function() { $(document).ready(function(){ $('#content').css('background-color','PapayaWhip');$('h1, h2, span, p, div').css('color','black'); }); })()"
        mWebView.evaluateJavaScript(JS, completionHandler: nil)

    }
    
    @objc func mWhiteClicked() {
//        hideStuff()
                mHeader.setIcon(prefixText: "", prefixTextColor: .white, icon: .fontAwesomeSolid(.book), iconColor: UIColor(named: "warning")!, postfixText: NSLocalizedString("learn", comment: ""), postfixTextColor: UIColor(named: "warning")!, size: nil, iconSize: nil)
        view.backgroundColor = .white
        mThemeLbl.textColor = UIColor(named: "warning")!
        mPreferences.set(1, forKey: "PREF_LEARNING_THEME")
        mPreferences.synchronize()
        
        let JS = "javascript:(function() { $(document).ready(function(){ $('#content').css('background-color','white');$('h1, h2, span, p, div').css('color','black'); }); })()"
        mWebView.evaluateJavaScript(JS, completionHandler: nil)
    }
    
    @objc func mDarkClicked() {
//        hideStuff()
        mHeader.setIcon(prefixText: "", prefixTextColor: .white, icon: .fontAwesomeSolid(.book), iconColor: .white, postfixText: NSLocalizedString("learn", comment: ""), postfixTextColor: .white, size: nil, iconSize: nil)
        view.backgroundColor = UIColor(named: "CanonicalAubergine")!
        mThemeLbl.textColor = .white
        mPreferences.set(2, forKey: "PREF_LEARNING_THEME")
        mPreferences.synchronize()
        
        let JS = "javascript:(function() { $(document).ready(function(){ $('#content').css('background-color','#100C08');$('h1, h2, span, p, div').css('color','Seashell');$('.infobox h1, .infobox h2, .infobox span, .infobox p, .infobox div, .infobox').css('color','black'); }); })()"
        mWebView.evaluateJavaScript(JS, completionHandler: nil)
    }
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        mWebView.isHidden = true
        
        if let theme = mPreferences.object(forKey: "PREF_LEARNING_THEME") {
        switch theme as! Int {
        case 0:
            mSolarizedClicked()
        case 1:
            mWhiteClicked()
        case 2:
            mDarkClicked()
        default:
            break
        }
        } else {
            mSolarizedClicked()
        }
        
        mWebView.load(URLRequest(url: URL(string: mUrl!.replacingOccurrences(of: ".wikipedia", with: ".m.wikipedia"))!))

        mThemeLbl.text = NSLocalizedString("learning_theme", comment: "")
        
        let tapGestSolarized = UITapGestureRecognizer(target: self, action: #selector(mSolarizedClicked))
        mSolarized.addGestureRecognizer(tapGestSolarized)
        
        let tapGestWhite = UITapGestureRecognizer(target: self, action: #selector(mWhiteClicked))
        mWhite.addGestureRecognizer(tapGestWhite)
        
        let tapGestDark = UITapGestureRecognizer(target: self, action: #selector(mDarkClicked))
        mDark.addGestureRecognizer(tapGestDark)
        
//        let source = """
//        document.body.syle.background = "#777";
//        """
//        let userScript = WKUserScript(source: source, injectionTime: .atDocumentEnd, forMainFrameOnly: true)
//        let userContentController = WKUserContentController()
//        userContentController.addUserScript(userScript)
//        let confguration = WKWebViewConfguration()
//        confguration.userContentController = userContentController
//        self.webView = WKWebView(frame: self.view.bounds,
//        confguration: confguration)
        let JS = """
        javascript:(function() {
        var parent = document.getElementsByTagName('head').item(0);
        var script = document.createElement('script');
        script.crossorigin = 'anonymous';
        script.type = 'text/javascript';
        script.integrity = 'sha256-pasqAKBDmFT4eHoN2ndd6lN370kFiGUFyTiUHWhU7k8=';
        script.src = 'https://code.jquery.com/jquery-3.4.1.slim.min.js';
        parent.appendChild(script);
        })()
        """
        mWebView.evaluateJavaScript(JS, completionHandler: nil)
        mTimer = Timer.scheduledTimer(timeInterval: 1.0, target: self, selector: #selector(hideStuff), userInfo: nil, repeats: true)
    }
    

    /*
    // MARK: - Navigation

    // In a storyboard-based application, you will often want to do a little preparation before navigation
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        // Get the new view controller using segue.destination.
        // Pass the selected object to the new view controller.
    }
    */

}
