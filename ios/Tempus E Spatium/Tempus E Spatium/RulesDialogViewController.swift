//
//  RulesDialogViewController.swift
//  Tempus E Spatium
//
//  Created by Victor Poon on 20/10/2019.
//  Copyright © 2019 SoftFeta. All rights reserved.
//

import UIKit
import SwiftIcons

class RulesDialogViewController: UIViewController {

    @IBOutlet weak var mRulesLbl: UILabel!
    
    @IBOutlet weak var mRulesTxt: UILabel!
    
    override func viewDidLoad() {
        super.viewDidLoad()

        mRulesLbl!.setIcon(prefixText: "", prefixTextFont: mRulesLbl.font!, prefixTextColor: .white, icon: .fontAwesomeBrands(.leanpub), iconColor: UIColor(named: "PapayaWhip")!, postfixText: NSLocalizedString("rules", comment: ""), postfixTextFont: mRulesLbl.font!, postfixTextColor: .white, iconSize: nil)
        mRulesTxt.text = NSLocalizedString("rules_tirade", comment: "")
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
