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

    @IBOutlet weak var mHeader: UILabel!
    
    @IBOutlet weak var mWebView: WKWebView!
    
    @IBOutlet weak var mThemeLbl: UILabel!
    var mUrl:String?
    
    @IBOutlet weak var mSolarized: UIView!
    @IBOutlet weak var mWhite: UIView!
    @IBOutlet weak var mDark: UIView!
    
    @objc func mSolarizedClicked() {
                mHeader.setIcon(prefixText: "", prefixTextColor: .white, icon: .fontAwesomeSolid(.book), iconColor: UIColor(named: "warning")!, postfixText: NSLocalizedString("learn", comment: ""), postfixTextColor: UIColor(named: "warning")!, size: nil, iconSize: nil)
        view.backgroundColor = UIColor(named: "CosmicLatte")!
    }
    
    @objc func mWhiteClicked() {
                mHeader.setIcon(prefixText: "", prefixTextColor: .white, icon: .fontAwesomeSolid(.book), iconColor: UIColor(named: "warning")!, postfixText: NSLocalizedString("learn", comment: ""), postfixTextColor: UIColor(named: "warning")!, size: nil, iconSize: nil)
        view.backgroundColor = .white
    }
    
    @objc func mDarkClicked() {
        mHeader.setIcon(prefixText: "", prefixTextColor: .white, icon: .fontAwesomeSolid(.book), iconColor: .white, postfixText: NSLocalizedString("learn", comment: ""), postfixTextColor: .white, size: nil, iconSize: nil)
        view.backgroundColor = UIColor(named: "CanonicalAubergine")!
    }
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        mHeader.setIcon(prefixText: "", prefixTextColor: .white, icon: .fontAwesomeSolid(.book), iconColor: UIColor(named: "warning")!, postfixText: NSLocalizedString("learn", comment: ""), postfixTextColor: UIColor(named: "warning")!, size: nil, iconSize: nil)
        mWebView.load(URLRequest(url: URL(string: mUrl!)!))

        mThemeLbl.text = NSLocalizedString("learning_theme", comment: "")
        
        let tapGestSolarized = UITapGestureRecognizer(target: self, action: #selector(mSolarizedClicked))
        mSolarized.addGestureRecognizer(tapGestSolarized)
        
        let tapGestWhite = UITapGestureRecognizer(target: self, action: #selector(mWhiteClicked))
        mWhite.addGestureRecognizer(tapGestWhite)
        
        let tapGestDark = UITapGestureRecognizer(target: self, action: #selector(mDarkClicked))
        mDark.addGestureRecognizer(tapGestDark)
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
