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
    
    @IBOutlet weak var mTextField: UITextField!
    
    @IBOutlet weak var mDoneBtn: UIButton!
    
    @IBAction func mDoneBtnClicked() {
        if mTextField.hasText {
            HighscoresDBManager.shared.insertHighscore(player: mTextField.text!, score: 123)
            dismiss(animated: true) {
                self.performSegue(withIdentifier: "inputToHighscores", sender: nil)
            }
        }
    }
    
    override func viewDidLoad() {
        super.viewDidLoad()

        mWonLbl!.setIcon(prefixText: "", prefixTextFont: mWonLbl.font!, prefixTextColor: .white, icon: .googleMaterialDesign(.sentimentVerySatisfied), iconColor: UIColor(named: "PapayaWhip")!, postfixText: NSLocalizedString("highscores_enter_name", comment: ""), postfixTextFont: mWonLbl.font!, postfixTextColor: UIColor(named: "PapayaWhip")!, iconSize: nil)
        
        mDoneBtn.setIcon(prefixText: "", prefixTextColor: .white, icon: .fontAwesomeSolid(.paperPlane), iconColor: .white, postfixText: NSLocalizedString("submit", comment: ""), postfixTextColor: .white, backgroundColor: UIColor(named: "success")!, forState: .normal, textSize: nil, iconSize: nil)
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
