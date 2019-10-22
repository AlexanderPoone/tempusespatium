//
//  InputHighscoreViewController.swift
//  Tempus E Spatium
//
//  Created by Victor Poon on 23/10/2019.
//  Copyright © 2019 SoftFeta. All rights reserved.
//

import UIKit
import SwiftIcons

class InputHighscoreViewController: UIViewController {

    @IBOutlet weak var mWonLbl: UILabel!
    
    override func viewDidLoad() {
        super.viewDidLoad()

        mWonLbl!.setIcon(prefixText: "", prefixTextFont: mWonLbl.font!, prefixTextColor: .white, icon: .googleMaterialDesign(.sentimentVerySatisfied), iconColor: UIColor(named: "PapayaWhip")!, postfixText: NSLocalizedString("highscores_enter_name", comment: ""), postfixTextFont: mWonLbl.font!, postfixTextColor: UIColor(named: "PapayaWhip")!, iconSize: nil)
        
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
