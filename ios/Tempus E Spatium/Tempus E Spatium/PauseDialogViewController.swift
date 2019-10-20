//
//  PauseDialogViewController.swift
//  Tempus E Spatium
//
//  Created by Victor Poon on 20/10/2019.
//  Copyright © 2019 SoftFeta. All rights reserved.
//

import UIKit
import SwiftIcons

class PauseDialogViewController: UIViewController {
    
    @IBOutlet weak var mGamePausedLbl: UILabel!
    
    @IBOutlet weak var mResumeBtn: UIButton!
    
    @IBOutlet weak var mRulesBtn: UIButton!
    
    @IBOutlet weak var mQuitBtn: UIButton!
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        mGamePausedLbl!.setIcon(prefixText: "", prefixTextFont: mGamePausedLbl.font!, prefixTextColor: .white, icon: .fontAwesomeSolid(.pause), iconColor: .white, postfixText: NSLocalizedString("paused", comment: ""), postfixTextFont: mGamePausedLbl.font!, postfixTextColor: .white, iconSize: nil)
        
        mResumeBtn!.setIcon(prefixText: "", prefixTextColor: .white, icon: .fontAwesomeSolid(.playCircle), iconColor: .white, postfixText: NSLocalizedString("resume", comment: ""), postfixTextColor: .white, backgroundColor: UIColor(named: "info")!, forState: .normal, textSize: nil, iconSize: nil)
        
        mRulesBtn!.setIcon(prefixText: "", prefixTextColor: .white, icon: .fontAwesomeBrands(.leanpub), iconColor: .white, postfixText: NSLocalizedString("rules", comment: ""), postfixTextColor: .white, backgroundColor: UIColor(named: "success")!, forState: .normal, textSize: nil, iconSize: nil)
        
        mQuitBtn!.setIcon(prefixText: "", prefixTextColor: .white, icon: .fontAwesomeSolid(.signOutAlt), iconColor: .white, postfixText: NSLocalizedString("return_main_menu", comment: ""), postfixTextColor: .white, backgroundColor: UIColor(named: "danger")!, forState: .normal, textSize: nil, iconSize: nil)
        
    }
    
}
